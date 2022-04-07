package org.opentele.consult.contract.security;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.security.UserType;

public class OrganisationUserRequest extends UserContract {
    private String password;
    private int organisationId;
    private ProviderType providerType;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(int organisationId) {
        this.organisationId = organisationId;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public User toUser(Organisation organisation, String hashedPassword) {
        User user = new User();
        this.mapToUser(user);
        user.setPassword(hashedPassword);
        user.addProviderType(providerType, UserType.User, organisation);
        return user;
    }
}
