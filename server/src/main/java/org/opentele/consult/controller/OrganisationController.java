package org.opentele.consult.controller;

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
    public List<UserContract> getUsers(Principal principal) {
        String userId = principal.getName();
        return userService.getOrganisationUsers(userId).stream().map(OrganisationUserContract::create).collect(Collectors.toList());
    }

    @PostMapping("/api/organisation/addUser")
    @PreAuthorize("hasRole('OrgAdmin')")
    public ResponseEntity addUser(@RequestParam(name = "userName") String userName, @RequestParam(name = "providerType") String providerType) {
        User user = userService.getUser(userName);
        if (user == null)
            return new ResponseEntity("added-user-not-found", HttpStatus.BAD_REQUEST);

        OrganisationUser organisationUser = userService.getOrganisationUser(userName, getCurrentOrganisation());
        if (organisationUser != null)
            return new ResponseEntity("user-already-in-organisation", HttpStatus.BAD_REQUEST);

        userService.addUser(getCurrentOrganisation(), user, UserType.User, ProviderType.valueOf(providerType));
        return new ResponseEntity(HttpStatus.OK);
    }
}
