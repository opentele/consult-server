package org.opentele.consult.contract.security;

import org.opentele.consult.contract.framework.BaseEntityContract;
import org.opentele.consult.domain.security.User;

public class ProviderResponse extends BaseEntityContract {
    private String name;
    private String providerClientDisplay;

    public ProviderResponse() {
    }

    public ProviderResponse(User user) {
        super(user.getId());
        this.name = user.getName();
        this.providerClientDisplay = user.getDetailsForClient();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProviderClientDisplay() {
        return providerClientDisplay;
    }

    public void setProviderClientDisplay(String providerClientDisplay) {
        this.providerClientDisplay = providerClientDisplay;
    }
}
