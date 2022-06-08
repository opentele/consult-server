package org.opentele.consult.controller;

import org.opentele.consult.contract.security.OrganisationCreateRequest;
import org.opentele.consult.contract.security.UserAddRequest;
import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.security.UserType;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.service.OrganisationService;
import org.opentele.consult.service.OrganisationUserService;
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

import javax.transaction.Transactional;
import java.util.List;

@RestController
public class OrganisationController extends BaseController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final OrganisationService organisationService;
    private final OrganisationUserService ouService;

    @Autowired
    public OrganisationController(UserService userService, UserSession userSession, BCryptPasswordEncoder bCryptPasswordEncoder, OrganisationService organisationService, OrganisationUserService ouService) {
        super(userService, userSession);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.organisationService = organisationService;
        this.ouService = ouService;
    }

    @RequestMapping(value = "/api/organisation", method = {RequestMethod.PUT})
    @Transactional
    public ResponseEntity<String> save(@RequestBody OrganisationCreateRequest request) {
        String error = userService.validateNewUser(request.getEmail(), request.getMobile());
        if (error != null)
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);

        User user = userService.createUser(request.getName(), request.getEmail(), request.getMobile(), bCryptPasswordEncoder.encode(request.getPassword()), userService.getAppUser());
        Organisation organisation = organisationService.createOrg(request.getOrganisationName(), user);
        ouService.associateExistingUser(user, UserType.OrgAdmin, ProviderType.None, organisation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/organisation/addUsers", method = {RequestMethod.POST, RequestMethod.PUT})
    @PreAuthorize("hasRole('OrgAdmin')")
    public ResponseEntity<Void> addUsers(@RequestBody List<UserAddRequest> requests) {
        requests.forEach(this::addToOrganisation);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/organisation/addUser", method = {RequestMethod.POST})
    @PreAuthorize("hasRole('OrgAdmin')")
    public ResponseEntity<String> addToOrganisation(@RequestBody UserAddRequest userAddRequest) {
        User user = userService.getUser(userAddRequest.getUserId());
        if (user == null)
            return new ResponseEntity<>("added-user-not-found", HttpStatus.BAD_REQUEST);

        userService.addUser(getCurrentOrganisation(), user, UserType.User, ProviderType.valueOf(userAddRequest.getProviderType()));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
