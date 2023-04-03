package org.opentele.consult.repository.framework;

import org.opentele.consult.domain.Organisation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface AbstractRepository<T> extends CrudRepository<T, Long> {
    T findByIdAndOrganisation(long id, Organisation organisation);
    T findByUuidAndOrganisation(UUID uuid, Organisation organisation);
    default T findEntity(long id, Organisation organisation) {
        return findByIdAndOrganisation(id, organisation);
    }
    default T findEntityInternal(long id) {
        return findById(id).get();
    }
}
