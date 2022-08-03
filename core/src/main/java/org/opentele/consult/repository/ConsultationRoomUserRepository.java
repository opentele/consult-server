package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomScheduleUser;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomUser;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultationRoomUserRepository extends AbstractRepository<ConsultationRoomUser> {
    ConsultationRoomUser findByConsultationRoomAndUserIdAndOrganisation(
            ConsultationRoom consultationRoom, int userId, Organisation organisation);
}
