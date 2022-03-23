package org.opentele.consult.contract.security;

import org.opentele.consult.domain.security.OrganisationUser;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.security.UserType;

public class OrganisationUserContract extends UserContract {
    private UserType userType;
    private ProviderType providerType;
    private String organisationName;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public static OrganisationUserContract create(OrganisationUser organisationUser) {
        OrganisationUserContract userContract = new OrganisationUserContract();
        User user = organisationUser.getUser();
        userContract.setEmail(user.getEmail());
        userContract.setMobile(user.getMobile());
        userContract.setUserType(organisationUser.getUserType());
        userContract.setName(user.getName());
        return userContract;
    }
}
