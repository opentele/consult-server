package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.consultationRoom.ConsultationRooms;
import org.opentele.consult.domain.security.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationRoomRepository extends PagingAndSortingRepository<ConsultationRoom, Integer> {
    default ConsultationRooms findAllBetween(LocalDateTime from, LocalDateTime to, Organisation organisation) {
        return new ConsultationRooms(findAllByScheduledStartAtAfterAndScheduledEndAtBeforeAndOrganisation(from, to, organisation));
    }

    List<ConsultationRoom> findAllByScheduledStartAtAfterAndScheduledEndAtBeforeAndOrganisation(LocalDateTime from, LocalDateTime to, Organisation organisation);
}
