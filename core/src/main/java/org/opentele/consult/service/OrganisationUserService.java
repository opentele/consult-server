package org.opentele.consult.service;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.OrganisationUser;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.security.UserType;
import org.opentele.consult.repository.OrganisationUserRepository;
import org.opentele.consult.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganisationUserService {
    private final OrganisationUserRepository organisationUserRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrganisationUserService(OrganisationUserRepository organisationUserRepository, UserRepository userRepository) {
        this.organisationUserRepository = organisationUserRepository;
        this.userRepository = userRepository;
    }

    public OrganisationUser associateExistingUser(User user, UserType userType, ProviderType providerType, Organisation organisation) {
        OrganisationUser organisationUser = new OrganisationUser();
        organisationUser.setUserType(userType);
        organisationUser.setProviderType(providerType);
        organisationUser.setUser(user);
        organisationUser.setOrganisation(organisation);
        return organisationUserRepository.save(organisationUser);
    }

    public OrganisationUser createNewUser(User user, UserType userType, ProviderType providerType, Organisation organisation) {
        User savedUser = userRepository.save(user);
        return associateExistingUser(savedUser, userType, providerType, organisation);
    }
}
