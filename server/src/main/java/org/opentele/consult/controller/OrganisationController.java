package org.opentele.consult.controller;

import org.opentele.consult.contract.security.UserContract;
import org.opentele.consult.domain.security.OrganisationUser;
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
        return userService.getOrganisationUsers(userId).stream().map(UserContract::create).collect(Collectors.toList());
    }

    @PostMapping("/api/organisation/addUser")
    @PreAuthorize("hasRole('OrgAdmin')")
    public ResponseEntity addUser(@RequestParam(name = "userId") int userId) {
        OrganisationUser organisationUser = userService.getOrganisationUser(userId, getCurrentOrganisation());
        if (organisationUser == null) {
            userService.addUser(getCurrentOrganisation(), getUser(userId), UserType.User);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
