package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.opentele.consult.repository.projections.ConsultationRoomClientProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends AbstractRepository<Client> {
    @Query(value = "SELECT c.* FROM client c " +
            "join organisation o on c.organisation_id = o.id " +
            "where c.name ilike :s and o.id = :organisationId and c.id not in (select client_id from appointment_token where consultation_room_id = :consultationRoomId ) order by c.name limit 10",
            nativeQuery = true)
    List<Client> searchClients(String s, int organisationId, int consultationRoomId);

    @Query(value = "SELECT c.* FROM client c " +
            "join organisation o on c.organisation_id = o.id " +
            "where (c.name ilike :name and COALESCE(c.registration_number, '') ilike :registrationNumber) and o.id = :organisationId order by c.name limit 10",
            nativeQuery = true)
    List<Client> searchClients(String name, String registrationNumber, int organisationId);

    default List<Client> getClientsMatching(String s, Organisation organisation, int consultationRoomId) {
        return searchClients("%" + s + "%", organisation.getId(), consultationRoomId);
    }

    @Query(value = "SELECT c.name, c.registration_number as registrationNumber, c.gender, c.date_of_birth as dateOfBirth, at.queue_number as queueNumber FROM client c " +
            "join appointment_token at on c.id = at.client_id " +
            "where at.consultation_room_id = :consultationRoomId" +
            " order by c.name",
            nativeQuery = true)
    List<ConsultationRoomClientProjection> getClients(int consultationRoomId);

    default List<Client> getClientsMatching(String name, String registrationNumber, Organisation organisation) {
        String s2 = registrationNumber == null ? "" : registrationNumber;
        return searchClients("%" + name + "%", "%" + s2 + "%", organisation.getId());
    }

    int countAllByOrganisation(Organisation organisation);

    List<Client> findTop10ByRegistrationNumberContainingAndOrganisation(String q, Organisation organisation);
}
