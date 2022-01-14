package org.opentele.consult.repository;

import org.opentele.consult.domain.security.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByEmail(String email);
    default User getUserByEmail(String email) {
        if (email == null) return null;
        return findByEmail(email);
    }

    User findByMobile(String mobile);
    default User getUserByMobile(String mobile) {
        if (mobile == null) return null;
        return findByMobile(mobile);
    }
}
