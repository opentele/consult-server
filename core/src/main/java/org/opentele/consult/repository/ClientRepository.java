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
            "where c.name ilike :s and o.id = :organisationId and c.id not in (select client_id from appointment where consultation_room_id = :consultationRoomId ) order by c.name limit 10",
            nativeQuery = true)
    List<Client> searchClients(String s, Long organisationId, long consultationRoomId);
    default List<Client> getClientsMatching(String s, Organisation organisation, long consultationRoomId) {
        return searchClients("%" + s + "%", organisation.getId(), consultationRoomId);
    }
    @Query(value = "SELECT count(c.id) FROM client c " +
            "join organisation o on c.organisation_id = o.id " +
            "where c.name ilike :s and o.id = :organisationId and c.id not in (select client_id from appointment where consultation_room_id = :consultationRoomId )",
            nativeQuery = true)
    int countClients(String s, long organisationId, long consultationRoomId);
    default int countClientsMatching(String s, Organisation organisation, long consultationRoomId) {
        return countClients("%" + s + "%", organisation.getId(), consultationRoomId);
    }

    @Query(value = "SELECT c.* FROM client c " +
            "join organisation o on c.organisation_id = o.id " +
            "where (c.name ilike :q or COALESCE(c.registration_number, '') ilike :q) and o.id = :organisationId order by c.name limit 10",
            nativeQuery = true)
    List<Client> searchClients(String q, long organisationId);
    default List<Client> searchClients(String s, Organisation organisation) {
        return searchClients("%" + s + "%", organisation.getId());
    }

    @Query(value = "SELECT c.id, c.name, c.registration_number as registrationNumber, c.gender, c.date_of_birth as dateOfBirth, a.queue_number as queueNumber FROM client c " +
            "join appointment a on c.id = a.client_id " +
            "where a.consultation_room_id = :consultationRoomId" +
            " order by c.name",
            nativeQuery = true)
    List<ConsultationRoomClientProjection> getClients(long consultationRoomId);

    @Query(value = "SELECT c.* FROM client c " +
            "join organisation o on c.organisation_id = o.id " +
            "where (c.name ilike :name and COALESCE(c.registration_number, '') ilike :registrationNumber) and o.id = :organisationId order by c.name limit 10",
            nativeQuery = true)
    List<Client> searchClients(String name, String registrationNumber, long organisationId);
    default List<Client> getClientsMatching(String name, String registrationNumber, Organisation organisation) {
        String s2 = registrationNumber == null ? "" : registrationNumber;
        return searchClients("%" + name + "%", "%" + s2 + "%", organisation.getId());
    }

    int countAllByOrganisation(Organisation organisation);
}
