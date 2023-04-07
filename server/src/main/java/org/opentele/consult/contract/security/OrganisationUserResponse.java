package org.opentele.consult.contract.security;

import org.opentele.consult.domain.Language;
import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.OrganisationUser;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.UserType;

public class OrganisationUserResponse extends UserContract {
    private UserType userType;
    private ProviderType providerType;
    private Language language;

    public UserType getUserType() {
        return userType;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public Language getLanguage() {
        return language;
    }

    protected void populate(OrganisationUser organisationUser) {
        this.userType = organisationUser.getUserType();
        this.providerType = organisationUser.getProviderType();
        this.language = organisationUser.getLanguage();
    }

    public static OrganisationUserResponse from(OrganisationUser organisationUser) {
        OrganisationUserResponse response = new OrganisationUserResponse();
        UserContract.from(organisationUser.getUser(), response);
        response.populate(organisationUser);
        return response;
    }
}
