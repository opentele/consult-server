package org.opentele.consult.controller;

import org.opentele.consult.contract.security.OrganisationUserContract;
import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.service.UserService;

import java.security.Principal;

public abstract class BaseController {
    protected final UserService userService;
    private UserSession userSession;

    public BaseController(UserService userService, UserSession userSession) {
        this.userService = userService;
        this.userSession = userSession;
    }

    protected Organisation getCurrentOrganisation() {
        int currentOrganisationId = userSession.getCurrentOrganisationId();
        return userService.getOrganisation(currentOrganisationId);
    }

    protected User getCurrentUser(Principal principal) {
        return userService.getUser(principal);
    }

    protected User getUser(int userId) {
        return userService.getUser(userId);
    }
}
