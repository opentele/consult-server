package org.opentele.consult.repository.framework;

import org.opentele.consult.domain.Organisation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface AbstractRepository<T> extends CrudRepository<T, Integer> {
    T findByIdAndOrganisation(int id, Organisation organisation);
    T findByUuidAndOrganisation(UUID uuid, Organisation organisation);
    default T findEntity(int id, Organisation organisation) {
        return findByIdAndOrganisation(id, organisation);
    }
}
