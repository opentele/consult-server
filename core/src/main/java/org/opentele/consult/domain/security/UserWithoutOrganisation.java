package org.opentele.consult.domain.security;

import org.opentele.consult.domain.NullOrganisation;
import org.opentele.consult.domain.Organisation;

public class UserWithoutOrganisation extends OrganisationUser {
    public UserWithoutOrganisation(User user) {
        this.setUser(user);
    }

    @Override
    public UserType getUserType() {
        return UserType.NonOrgUser;
    }

    @Override
    public ProviderType getProviderType() {
        return ProviderType.None;
    }

    @Override
    public Organisation getOrganisation() {
        return new NullOrganisation();
    }
}
