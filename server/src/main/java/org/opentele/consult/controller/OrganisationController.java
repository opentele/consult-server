package org.opentele.consult.controller;

import org.opentele.consult.contract.security.AddUserRequest;
import org.opentele.consult.contract.security.OrganisationUserContract;
import org.opentele.consult.contract.security.UserContract;
import org.opentele.consult.domain.security.OrganisationUser;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.security.UserType;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrganisationController extends BaseController {
    @Autowired
    public OrganisationController(UserService userService, UserSession userSession) {
        super(userService, userSession);
    }

    @RequestMapping(value = "/api/organisation/user", method = {RequestMethod.GET})
    @PreAuthorize("hasRole('OrgAdmin')")
    public List<OrganisationUserContract> getUsers(@RequestParam(name = "providerType", required = false) ProviderType providerType) {
        return userService.getOrganisationUsers(getCurrentOrganisation(), providerType).stream().map(OrganisationUserContract::create).collect(Collectors.toList());
    }

    @RequestMapping(value = "/api/organisation/users", method = {RequestMethod.POST, RequestMethod.PUT})
    @PreAuthorize("hasRole('OrgAdmin')")
    public ResponseEntity<Void> addUsers(@RequestBody List<AddUserRequest> addUserRequests) {
        addUserRequests.forEach(addUserRequest -> this.addUser(addUserRequest.getUserId(), addUserRequest.getProviderType()));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/api/organisation/user", method = {RequestMethod.POST, RequestMethod.PUT})
    @PreAuthorize("hasRole('OrgAdmin')")
    public ResponseEntity<String> addUser(@RequestParam(name = "userId") int userId, @RequestParam(name = "providerType") String providerType) {
        User user = userService.getUser(userId);
        if (user == null)
            return new ResponseEntity<>("added-user-not-found", HttpStatus.BAD_REQUEST);

        userService.addUser(getCurrentOrganisation(), user, UserType.User, ProviderType.valueOf(providerType));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
