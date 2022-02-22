package org.opentele.consult.repository;

import org.opentele.consult.domain.consultationRoom.AppointmentToken;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentTokenRepository extends AbstractRepository<AppointmentToken> {
    AppointmentToken findFirstByConsultationRoomAndQueueNumberGreaterThanOrderByQueueNumber(ConsultationRoom consultationRoom, int queueNumber);
    AppointmentToken findFirstByConsultationRoomAndQueueNumberGreaterThanAndAppointmentProviderOrderByQueueNumber(ConsultationRoom consultationRoom, int queueNumber, User user);
    AppointmentToken findTopByConsultationRoomOrderByQueueNumberDesc(ConsultationRoom consultationRoom);
    default int getMostRecentAppointmentQueueNumber(ConsultationRoom consultationRoom) {
        AppointmentToken appointmentToken = this.findTopByConsultationRoomOrderByQueueNumberDesc(consultationRoom);
        return appointmentToken == null ? 0 : appointmentToken.getQueueNumber();
    }
}
