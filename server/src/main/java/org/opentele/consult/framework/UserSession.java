package org.opentele.consult.framework;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@Scope(value="session", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class UserSession implements Serializable {
    private long currentOrganisationId;
    private long userId;

    public long getCurrentOrganisationId() {
        return currentOrganisationId;
    }

    public void setCurrentOrganisationId(long currentOrganisationId) {
        this.currentOrganisationId = currentOrganisationId;
    }

    public long getUserId() {
        return userId;
    }

    public void setCurrentUserId(long userId) {
        this.userId = userId;
    }
}
