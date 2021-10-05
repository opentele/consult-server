package org.opentele.consult.repository.framework;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface AbstractRepository<T> extends CrudRepository<T, Integer> {
    T findByUuid(UUID uuid);
    default T findEntity(int id) {
        Optional<T> t = findById(id);
        return t.orElse(null);
    }
}
