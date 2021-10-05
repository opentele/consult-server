package org.opentele.consult.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRepository extends CrudRepository<OrganisationRepository, Integer> {
}
