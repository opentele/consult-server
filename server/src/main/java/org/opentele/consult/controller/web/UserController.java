package org.opentele.consult.controller.web;

import org.opentele.consult.contract.OrganisationCreateRequest;
import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.security.UserType;
import org.opentele.consult.framework.Translator;
import org.opentele.consult.repository.OrganisationRepository;
import org.opentele.consult.repository.UserRepository;
import org.opentele.consult.repository.framework.Repository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/")
public class UserController {
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EntityManager entityManager;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder, EntityManager entityManager) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.entityManager = entityManager;
    }

    @RequestMapping(value = "users", method = {RequestMethod.PUT})
    @Transactional
    @PreAuthorize("hasRole('Users_Write')")
    public ResponseEntity<String> save(@RequestBody OrganisationCreateRequest organisationCreateRequest) {
        Set<String> errors = userService.validateNewOrganisation(organisationCreateRequest.getName(), organisationCreateRequest.getEmail());
        if (errors.size() != 0)
            return new ResponseEntity<>(Translator.fromErrors(errors), HttpStatus.CONFLICT);

        Organisation organisation = new Organisation();
        organisation.setName(organisationCreateRequest.getOrganisationName());


        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setPassword(bCryptPasswordEncoder.encode(organisationCreateRequest.getPassword()));
        user.setEmail(organisationCreateRequest.getEmail());
        user.setName(organisationCreateRequest.getName());
        user.setUserType(UserType.OrgAdmin);

        userService.createNewOrganisation(user, organisation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "currentUser", method = RequestMethod.GET)
    @PreAuthorize("hasRole('User')")
    public User loggedInUser(Principal principal) {
        String name = principal.getName();
        return userService.findUserByEmail(name);
    }
}
