package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganisationRepository extends CrudRepository<Organisation, Integer> {
    Organisation findByName(String name);
    default Organisation findEntity(int id) {
        if (id == 0) return null;
        return findById(id).get();
    }
}
