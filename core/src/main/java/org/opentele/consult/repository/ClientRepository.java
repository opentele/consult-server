package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.opentele.consult.repository.projections.ConsultationRoomClientProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends AbstractRepository<Client> {
    @Query(
            value = "SELECT c.* FROM client c " +
                    "join organisation o on c.organisation_id = o.id " +
                    "where c.name like :s and o.id = :organisationId and c.id not in (select client_id from appointment_token where consultation_room_id = :consultationRoomId ) order by c.name",
            nativeQuery = true)
    List<Client> searchClients(String s, int organisationId, int consultationRoomId);

    default List<Client> getClientsMatching(String s, Organisation organisation, int consultationRoomId) {
        return searchClients("%" + s + "%", organisation.getId(), consultationRoomId);
    }

    @Query(
            value = "SELECT c.name, c.registration_number as registrationNumber, c.gender, c.date_of_birth as dateOfBirth, at.queue_number as queueNumber FROM client c " +
                    "join appointment_token at on c.id = at.client_id " +
                    "where at.consultation_room_id = :consultationRoomId" +
                    " order by c.name",
            nativeQuery = true)
    List<ConsultationRoomClientProjection> getClients(int consultationRoomId);
}
