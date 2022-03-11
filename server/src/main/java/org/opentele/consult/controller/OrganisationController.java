package org.opentele.consult.controller;

import org.opentele.consult.contract.security.UserContract;
import org.opentele.consult.contract.security.UserOrganisationContract;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrganisationController {
    private final UserService userService;

    @Autowired
    public OrganisationController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/api/organisation/user", method = {RequestMethod.GET})
    @PreAuthorize("hasRole('OrgAdmin')")
    public List<UserContract> getUsers(Principal principal) {
        String userId = principal.getName();
        return userService.getOrganisationUsers(userId).stream().map(UserContract::create).collect(Collectors.toList());
    }
}
