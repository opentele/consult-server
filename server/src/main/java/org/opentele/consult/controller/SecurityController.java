package org.opentele.consult.controller;

import org.opentele.consult.contract.ApplicationStatus;
import org.opentele.consult.contract.UserCreateRequest;
import org.opentele.consult.controller.framework.AbstractController;
import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.security.UserType;
import org.opentele.consult.repository.OrganisationRepository;
import org.opentele.consult.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
public class SecurityController extends AbstractController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final OrganisationRepository organisationRepository;

    @Autowired
    public SecurityController(BCryptPasswordEncoder bCryptPasswordEncoder, UserRepository userRepository, OrganisationRepository organisationRepository) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.organisationRepository = organisationRepository;
    }

    private User saveUser(Organisation organisation, String userName, String email, String phone, UserType userType, String password) {
        User user = new User();
        user.setName(userName);
        user.setEmail(email);
        user.setMobile(phone);
        user.setUserType(userType);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setOrganisation(organisation);
        return userRepository.save(user);
    }

    @RequestMapping(value = "/api/app/user", method = {RequestMethod.POST, RequestMethod.PUT})
    @PreAuthorize("hasAnyRole('OrgAdmin')")
    @Transactional
    public ResponseEntity<ApplicationStatus> createUser(UserCreateRequest request) {
        Organisation organisation = organisationRepository.findEntity(request.getOrganisationId());
        saveUser(organisation, toString(), request.getEmail(), request.getPhone(), request.getUserType(), request.getPassword());
        return createSuccessResponse();
    }

    @GetMapping("/api/test/passwordHash")
    public String getPasswordHash(@RequestParam("password") String password) {
        return bCryptPasswordEncoder.encode(password);
    }
}
