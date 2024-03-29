package org.opentele.consult.contract.security;

import org.opentele.consult.domain.Language;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.UserType;

public class OrganisationUserPutPostRequest extends UserContract {
    private ProviderType providerType;
    private UserType userType;
    private Language language;

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

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }
}
