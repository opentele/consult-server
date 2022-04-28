package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedules;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ConsultationRoomScheduleRepository extends AbstractRepository<ConsultationRoomSchedule> {
    List<ConsultationRoomSchedule> findAllByOrganisation(Organisation organisation);
    List<ConsultationRoomSchedule> findAllBy();
}
