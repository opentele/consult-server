package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.OrganisationUser;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganisationUserRepository extends AbstractRepository<OrganisationUser> {
    OrganisationUser findByUserAndOrganisation(User user, Organisation currentOrganisation);
    OrganisationUser findByUserAndOrganisationId(User user, int organisationId);
    List<OrganisationUser> findAllByOrganisationName(String organisationName);
    List<OrganisationUser> findAllByUser(User user);
}
