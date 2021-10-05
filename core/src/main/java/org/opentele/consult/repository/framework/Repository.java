package org.opentele.consult.repository.framework;

import org.opentele.consult.domain.framework.AbstractEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Repository {
    public static <T> Set<T> findByIds(List<Integer> ids, AbstractRepository<T> abstractRepository) {
        return ids.stream().map(id -> Repository.findById(id, abstractRepository)).collect(Collectors.toSet());
    }

    public static <T> T findById(Integer id, AbstractRepository<T> crudRepository) {
        return (id == null || id == 0) ? null : (T) crudRepository.findById(id);
    }

    public static <T> T findByUuidOrId(UUID uuid, Integer id, AbstractRepository<T> abstractRepository) {
        // Simplifying based IntelliJ's suggestion could lead to recursive loop
        return Repository.findByUuidOrId(uuid == null ? null : uuid.toString(), id, abstractRepository);
    }

    public static <T> T findByUuidOrId(String uuid, Integer id, AbstractRepository<T> abstractRepository) {
        if (uuid == null) {
            return findById(id, abstractRepository);
        } else {
            return abstractRepository.findByUuid(UUID.fromString(uuid));
        }
    }

    public static <T extends AbstractEntity> T findByIdOrCreate(Integer id, AbstractRepository<T> repository, T newEntity) {
        if (id == null || id == 0) {
            return newEntity;
        }
        T t = findById(id, repository);
        if (t == null) {
            if (newEntity instanceof AbstractEntity)
                ((AbstractEntity)newEntity).setInactive(false);
            return newEntity;
        }
        return t;
    }

    public static <T extends AbstractEntity> T findByUuidOrCreate(String uuid, AbstractRepository<T> abstractRepository, T newEntity) {
        if (uuid == null || uuid.isEmpty()) {
            newEntity.setUuid(UUID.randomUUID());
            return newEntity;
        }
        T entity = abstractRepository.findByUuid(UUID.fromString(uuid));
        if (entity == null) {
            newEntity.setUuid(UUID.fromString(uuid));
            newEntity.setInactive(false);
            entity = newEntity;
        }
        return entity;
    }

    public static <T extends AbstractEntity> void mergeChildren(List<Integer> proposedChildrenIds, List<Integer> existingChildrenIds, AbstractRepository<T> childRepository, Consumer<AbstractEntity> removeChild, Consumer<AbstractEntity> addChild) {
        Set<Integer> proposedChildrenIdSet = new HashSet<>(proposedChildrenIds);
        HashSet<Integer> toRemoveChildrenIds = new HashSet<>(existingChildrenIds);
        toRemoveChildrenIds.removeAll(proposedChildrenIdSet);
        toRemoveChildrenIds.forEach(existingChildId -> removeChild.accept(childRepository.findById(existingChildId).get()));

        for (Integer proposedChildId : proposedChildrenIdSet) {
            addChild.accept(childRepository.findById(proposedChildId).get());
        }
    }

    public static <T extends AbstractEntity> T delete(Integer id, CrudRepository<T, Integer> repository) {
        T t = repository.findById(id).get();
        repository.delete(t);
        return t;
    }
}
