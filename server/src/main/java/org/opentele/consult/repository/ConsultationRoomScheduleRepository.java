package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ConsultationRoomScheduleRepository extends PagingAndSortingRepository<ConsultationRoomSchedule, Integer> {
    List<ConsultationRoomSchedule> findAllByOrganisation(Organisation organisation);
}
