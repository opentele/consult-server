package org.opentele.consult.framework;

import org.opentele.consult.domain.Organisation;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode= ScopedProxyMode.TARGET_CLASS)
public class UserSession {
    private Organisation currentOrganisation;

    public Organisation getCurrentOrganisation() {
        return currentOrganisation;
    }

    public void setCurrentOrganisation(Organisation currentOrganisation) {
        this.currentOrganisation = currentOrganisation;
    }
}
