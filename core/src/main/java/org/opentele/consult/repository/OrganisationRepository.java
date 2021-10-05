package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRepository extends AbstractRepository<Organisation> {
    Organisation findByName(String name);
}
