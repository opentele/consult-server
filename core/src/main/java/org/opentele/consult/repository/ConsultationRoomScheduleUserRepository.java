package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomScheduleUser;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationRoomScheduleUserRepository extends AbstractRepository<ConsultationRoomScheduleUser> {
    ConsultationRoomScheduleUser findByConsultationRoomScheduleAndUserIdAndOrganisation(
            ConsultationRoomSchedule consultationRoomSchedule, int userId, Organisation organisation);
}
