package org.opentele.consult.repository;

import org.opentele.consult.domain.security.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    default User findUser(long id) {
        return findById(id).get();
    }

    User findByEmail(String email);
    default User getUserByEmail(String email) {
        if (email == null) return null;
        return findByEmail(email);
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    default User getUserForAudit(String email) {
        return findByEmail(email);
    }

    User findByMobile(String mobile);
    default User getUserByMobile(String mobile) {
        if (mobile == null) return null;
        return findByMobile(mobile);
    }

    List<User> findAllByOrganisationUsersNull();

    default List<User> findUsersWithNoOrganisation() {
        return findAllByOrganisationUsersNull();
    }
}
