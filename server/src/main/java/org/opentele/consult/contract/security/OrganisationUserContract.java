package org.opentele.consult.contract.security;

import org.opentele.consult.domain.Language;
import org.opentele.consult.domain.security.OrganisationUser;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.UserType;

public class OrganisationUserContract extends UserContract {
    private UserType userType;
    private ProviderType providerType;
    private String organisationName;
    private String formIoProjectId;
    private Language language;

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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getFormIoProjectId() {
        return formIoProjectId;
    }

    public void setFormIoProjectId(String formIoProjectId) {
        this.formIoProjectId = formIoProjectId;
    }

    public static OrganisationUserContract from(OrganisationUser organisationUser) {
        OrganisationUserContract contract = new OrganisationUserContract();
        UserContract.from(organisationUser.getUser(), contract);

        contract.setUserType(organisationUser.getUserType());
        contract.setOrganisationName(organisationUser.getOrganisation().getName());
        contract.setProviderType(organisationUser.getProviderType());
        contract.setLanguage(organisationUser.getLanguage());
        contract.setFormIoProjectId(organisationUser.getOrganisation().getFormIoProjectId());
        return contract;
    }
}
