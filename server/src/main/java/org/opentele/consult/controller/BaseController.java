package org.opentele.consult.controller;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.service.UserService;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.security.Principal;

public abstract class BaseController {
    protected final UserService userService;
    private final UserSession userSession;

    public BaseController(UserService userService, UserSession userSession) {
        this.userService = userService;
        this.userSession = userSession;
    }

    protected Organisation getCurrentOrganisation() {
        long currentOrganisationId = userSession.getCurrentOrganisationId();
        if (currentOrganisationId == 0) return null;
        return userService.getOrganisation(currentOrganisationId);
    }

    protected User getCurrentUser(Principal principal) {
        return userService.getUser(principal);
    }

    protected User getUser(int userId) {
        return userService.getUser(userId);
    }

    protected void rollback() {
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
    }

    protected void setCurrentOrganisationId(Long organisationId) {
        userSession.setCurrentOrganisationId(organisationId);
    }
}
