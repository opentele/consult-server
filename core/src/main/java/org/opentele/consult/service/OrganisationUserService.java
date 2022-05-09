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

import java.util.List;

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

    public OrganisationUser getOrganisationUser(User user) {
        List<OrganisationUser> organisationUsers = organisationUserRepository.findAllByUser(user);
//        currently only one org per user is supported
        if (organisationUsers.size() != 1)
            return null;

        return organisationUsers.get(0);
    }

    public OrganisationUser getOrganisationUser(User user, Organisation currentOrganisation) {
        return organisationUserRepository.findByUserAndOrganisation(user, currentOrganisation);
    }

    public OrganisationUser update(User user, UserType userType, ProviderType providerType, Organisation organisation) {
        OrganisationUser organisationUser = organisationUserRepository.findByUserAndOrganisation(user, organisation);
        organisationUser.setUserType(userType);
        organisationUser.setProviderType(providerType);
        return organisationUserRepository.save(organisationUser);
    }
}
