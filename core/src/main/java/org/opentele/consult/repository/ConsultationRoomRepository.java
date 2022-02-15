package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.consultationRoom.ConsultationRooms;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Repository
public interface ConsultationRoomRepository extends PagingAndSortingRepository<ConsultationRoom, Integer> {
    boolean existsByConsultationRoomScheduleAndScheduledOn(ConsultationRoomSchedule schedule, LocalDate date);

    default ConsultationRooms findAllBetween(LocalDate from, LocalDate to, Organisation organisation) {
        return new ConsultationRooms(findAllByScheduledOnAfterAndScheduledOnBeforeAndOrganisation(from.minus(1, DAYS), to.plus(1, DAYS), organisation));
    }

    List<ConsultationRoom> findAllByScheduledOnAfterAndScheduledOnBeforeAndOrganisation(LocalDate from, LocalDate to, Organisation organisation);
}
