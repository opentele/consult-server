package org.opentele.consult.repository.framework;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.framework.AbstractAuditableEntity;
import org.opentele.consult.domain.framework.AbstractEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Repository {
    public static <T> Set<T> findByIds(List<Long> ids, Organisation organisation, AbstractRepository<T> abstractRepository) {
        return ids.stream().map(id -> Repository.findById(id, organisation, abstractRepository)).collect(Collectors.toSet());
    }

    public static <T> T findById(Long id, Organisation organisation, AbstractRepository<T> crudRepository) {
        return (id == null || id == 0) ? null : (T) crudRepository.findEntity(id, organisation);
    }

    public static <T> T findByUuidOrId(UUID uuid, Long id, Organisation organisation, AbstractRepository<T> abstractRepository) {
        // Simplifying based IntelliJ's suggestion could lead to recursive loop
        return Repository.findByUuidOrId(uuid == null ? null : uuid.toString(), id, organisation, abstractRepository);
    }

    public static <T> T findByUuidOrId(String uuid, Long id, Organisation organisation, AbstractRepository<T> abstractRepository) {
        if (uuid == null) {
            return findById(id, organisation, abstractRepository);
        } else {
            return abstractRepository.findByUuidAndOrganisation(UUID.fromString(uuid), organisation);
        }
    }

    public static <T extends AbstractAuditableEntity> T findByIdOrCreate(Long id, Organisation organisation, AbstractRepository<T> repository, T newEntity) {
        if (id == null || id == 0) {
            return newEntity;
        }
        var t = findById(id, organisation, repository);
        if (t == null) {
            if (newEntity != null)
                newEntity.setInactive(false);
            return newEntity;
        }
        return t;
    }

    public static <T extends AbstractAuditableEntity> T findByUuidOrCreate(String uuid, Organisation organisation, AbstractRepository<T> abstractRepository, T newEntity) {
        if (!StringUtils.hasText(uuid)) {
            newEntity.setUuid(UUID.randomUUID());
            return newEntity;
        }
        T entity = abstractRepository.findByUuidAndOrganisation(UUID.fromString(uuid), organisation);
        if (entity == null) {
            newEntity.setUuid(UUID.fromString(uuid));
            newEntity.setInactive(false);
            entity = newEntity;
        }
        return entity;
    }

    public static <T extends AbstractEntity> void mergeChildren(List<Long> proposedChildrenIds, List<Long> existingChildrenIds, Consumer<AbstractEntity> removeChild, Consumer<AbstractEntity> addChild, Function<Long, AbstractEntity> findEntity, Function<Long, AbstractEntity> createEntity) {
        Set<Long> proposedChildrenIdSet = new HashSet<>(proposedChildrenIds);
        HashSet<Long> toRemoveChildrenIds = new HashSet<>(existingChildrenIds);
        toRemoveChildrenIds.removeAll(proposedChildrenIdSet);
        proposedChildrenIdSet.removeAll(existingChildrenIds);
        toRemoveChildrenIds.forEach(existingChildId -> removeChild.accept(findEntity.apply(existingChildId)));

        for (Long proposedChildId : proposedChildrenIdSet) {
            addChild.accept(createEntity.apply(proposedChildId));
        }
    }

    public static <T extends AbstractAuditableEntity> T delete(Long id, Organisation organisation, AbstractRepository<T> repository) {
        T t = repository.findEntity(id, organisation);
        repository.delete(t);
        return t;
    }
}
