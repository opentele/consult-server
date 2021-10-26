package org.opentele.consult.repository;

import org.opentele.consult.domain.security.PasswordResetToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepositoryRepository extends CrudRepository<PasswordResetToken, Integer> {
    PasswordResetToken findByToken(String token);
}
