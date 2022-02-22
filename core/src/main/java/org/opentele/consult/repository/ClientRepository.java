package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends AbstractRepository<Client> {
    @Query(
            value = "SELECT * FROM client c " +
                    "join organisation o on c.organisation_id = o.id " +
                    "where c.name like :s and o.id = :organisationId and c.id not in (select id from appointment_token where consultation_room_id = :consultationRoomId ) order by c.name",
            nativeQuery = true)
    List<Client> searchClients(String s, int organisationId, int consultationRoomId);
    default List<Client> getClientsMatching(String s, Organisation organisation, int consultationRoomId) {
        return searchClients("%"+s+"%", organisation.getId(), consultationRoomId);
    }
}
