package org.opentele.consult.service;

import org.opentele.consult.domain.Language;
import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.framework.OrganisationalEntity;
import org.opentele.consult.domain.security.*;
import org.opentele.consult.repository.OrganisationUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class OrganisationUserService {
    private final OrganisationUserRepository organisationUserRepository;

    @Autowired
    public OrganisationUserService(OrganisationUserRepository organisationUserRepository) {
        this.organisationUserRepository = organisationUserRepository;
    }

    public OrganisationUser associateExistingUser(User user, UserType userType, ProviderType providerType, Organisation organisation, Language language) {
        OrganisationUser organisationUser = new OrganisationUser();
        organisationUser.setUserType(userType);
        organisationUser.setProviderType(providerType);
        organisationUser.setUser(user);
        organisationUser.setOrganisation(organisation);
        organisationUser.setLanguage(language == null ? Language.en : language);
        return organisationUserRepository.save(organisationUser);
    }

    public Organisation getOrganisation(User user) {
        List<OrganisationUser> organisationUsers = organisationUserRepository.findAllByUser(user);
//        currently only one org per user is supported
        List<Organisation> organisations = organisationUsers.stream().map(OrganisationalEntity::getOrganisation).distinct().toList();
        if (organisations.size() == 0) return null;
        return organisations.get(0);
    }

    public OrganisationUser getMostPrivilegedOrganisationUser(User user, Organisation organisation) {
        List<OrganisationUser> organisationUsers = organisationUserRepository.findAllByUserAndOrganisation(user, organisation);
        return  organisationUsers.stream().max(Comparator.comparing(o -> o.getUserType().getAccessLevel())).stream().findFirst().orElse(null);
    }

    public OrganisationUser getOrganisationUser(User user, Organisation organisation) {
        if (organisation == null) return new UserWithoutOrganisation(user);
        return organisationUserRepository.findByUserAndOrganisation(user, organisation);
    }

    public OrganisationUser update(User user, UserType userType, ProviderType providerType, Organisation organisation) {
        OrganisationUser organisationUser = organisationUserRepository.findByUserAndOrganisation(user, organisation);
        organisationUser.setUserType(userType);
        organisationUser.setProviderType(providerType);
        return organisationUserRepository.save(organisationUser);
    }
}
