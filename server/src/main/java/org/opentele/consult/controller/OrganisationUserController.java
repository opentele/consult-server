package org.opentele.consult.controller;

import org.opentele.consult.contract.security.OrganisationUserContract;
import org.opentele.consult.contract.security.OrganisationUserPutRequest;
import org.opentele.consult.domain.security.OrganisationUser;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.service.OrganisationUserService;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrganisationUserController extends BaseController {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final OrganisationUserService organisationUserService;

    @Autowired
    public OrganisationUserController(UserService userService, UserSession userSession, BCryptPasswordEncoder bCryptPasswordEncoder, OrganisationUserService organisationUserService) {
        super(userService, userSession);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.organisationUserService = organisationUserService;
    }

    @RequestMapping(value = "/api/organisationUser", method = {RequestMethod.GET})
    @PreAuthorize("hasRole('User')")
    public List<OrganisationUserContract> getUsers(@RequestParam(name = "providerType", required = false) ProviderType providerType) {
        return userService.getOrganisationUsers(getCurrentOrganisation(), providerType).stream().map(OrganisationUserContract::from).collect(Collectors.toList());
    }

    @RequestMapping(value = "/api/organisationUser/{id}", method = {RequestMethod.GET})
    @PreAuthorize("hasRole('User')")
    public OrganisationUserContract getOrganisationUser(@PathVariable("id") int id) {
        OrganisationUser organisationUser = userService.getOrganisationUser(id, getCurrentOrganisation());
        return OrganisationUserContract.from(organisationUser);
    }

    @RequestMapping(value = "/api/organisationUser", method = {RequestMethod.PUT})
    @PreAuthorize("hasRole('OrgAdmin')")
    public ResponseEntity createUser(@RequestBody OrganisationUserPutRequest request) {
        User user;
        OrganisationUser organisationUser;
        if (request.getId() > 0) {
            user = userService.getUser(request.getId());
            organisationUser = organisationUserService.associateExistingUser(user, request.getUserType(), request.getProviderType(), getCurrentOrganisation());
        } else {
            String error = userService.validateNewUser(request.getEmail(), request.getMobile());
            if (error != null)
                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
            user = request.toUser(getCurrentOrganisation(), bCryptPasswordEncoder.encode(request.getPassword()));
            organisationUser = organisationUserService.createNewUser(user, request.getUserType(), request.getProviderType(), getCurrentOrganisation());
        }
        return new ResponseEntity<>(OrganisationUserContract.from(organisationUser), HttpStatus.OK);
    }
}
