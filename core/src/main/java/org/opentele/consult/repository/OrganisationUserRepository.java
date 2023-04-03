package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.OrganisationUser;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganisationUserRepository extends AbstractRepository<OrganisationUser> {
    OrganisationUser findByUserAndOrganisation(User user, Organisation currentOrganisation);
    OrganisationUser findByUserAndOrganisationId(User user, long organisationId);
    List<OrganisationUser> findAllByOrganisationName(String organisationName);
    List<OrganisationUser> findAllByOrganisationOrderByUserName(Organisation organisation);
    List<OrganisationUser> findAllByUser(User user);
    List<OrganisationUser> findTop10ByUserEmailContainsOrUserMobileContainsAndOrganisation(String q1, String q2, Organisation organisation);
    List<OrganisationUser> findAllByOrganisationAndProviderTypeOrderByUserName(Organisation organisation, ProviderType providerType);
}
