package org.opentele.consult.contract.security;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.security.UserType;

public class OrganisationUserPutRequest extends UserContract {
    private ProviderType providerType;
    private UserType userType;

    public ProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public User toUser(Organisation organisation, String hashedPassword) {
        User user = new User();
        this.mapToUser(user);
        user.setPassword(hashedPassword);
        user.addProviderType(providerType, UserType.User, organisation);
        return user;
    }
}
