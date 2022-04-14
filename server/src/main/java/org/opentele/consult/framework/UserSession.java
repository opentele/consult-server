package org.opentele.consult.framework;

import org.opentele.consult.domain.security.User;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value="session", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class UserSession implements Serializable {
    private int currentOrganisationId;
    private User currentUser;

    public int getCurrentOrganisationId() {
        return currentOrganisationId;
    }

    public void setCurrentOrganisationId(int currentOrganisationId) {
        this.currentOrganisationId = currentOrganisationId;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
