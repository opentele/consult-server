package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.consultationRoom.ConsultationRooms;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Repository
public interface ConsultationRoomRepository extends AbstractRepository<ConsultationRoom> {
    boolean existsByConsultationRoomScheduleAndScheduledOn(ConsultationRoomSchedule schedule, LocalDate date);

    default ConsultationRooms findAllBetween(LocalDate from, LocalDate to, Organisation organisation) {
        return new ConsultationRooms(findAllByScheduledOnAfterAndScheduledOnBeforeAndOrganisation(from.minus(1, DAYS), to.plus(1, DAYS), organisation));
    }

    List<ConsultationRoom> findAllByScheduledOnAfterAndScheduledOnBeforeAndOrganisation(LocalDate from, LocalDate to, Organisation organisation);
}
