package org.opentele.consult.contract.security;

import org.opentele.consult.domain.security.OrganisationUser;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.UserType;

public class UserOrganisationContract {
    private int organisationId;
    private String organisationName;
    private UserType userType;
    private ProviderType providerType;

    public int getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(int organisationId) {
        this.organisationId = organisationId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public void setProviderType(ProviderType providerType) {
        this.providerType = providerType;
    }

    public static UserOrganisationContract create(OrganisationUser organisationUser) {
        UserOrganisationContract contract = new UserOrganisationContract();
        contract.setOrganisationId(organisationUser.getOrganisation().getId());
        contract.setUserType(organisationUser.getUserType());
        contract.setOrganisationName(organisationUser.getOrganisation().getName());
        contract.setProviderType(organisationUser.getProviderType());
        return contract;
    }
}