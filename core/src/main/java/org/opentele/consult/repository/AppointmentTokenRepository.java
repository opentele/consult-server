package org.opentele.consult.repository;

import org.opentele.consult.domain.consultationRoom.AppointmentToken;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentTokenRepository extends AbstractRepository<AppointmentToken> {
    AppointmentToken findFirstByConsultationRoomAndQueueNumberGreaterThanOrderByQueueNumber(ConsultationRoom consultationRoom, int queueNumber);
    default AppointmentToken getNextToken(ConsultationRoom consultationRoom, int queueNumber) {
        return findFirstByConsultationRoomAndQueueNumberGreaterThanOrderByQueueNumber(consultationRoom, queueNumber);
    }

    AppointmentToken findFirstByAppointmentProviderAndConsultationRoomAndQueueNumberGreaterThanOrderByQueueNumber(User user, ConsultationRoom consultationRoom, int queueNumber);
    default AppointmentToken getNextToken(ConsultationRoom consultationRoom, int queueNumber, User user) {
        return findFirstByAppointmentProviderAndConsultationRoomAndQueueNumberGreaterThanOrderByQueueNumber(user, consultationRoom, queueNumber);
    }

    AppointmentToken findLastByConsultationRoomAndQueueNumberLessThanOrderByQueueNumber(ConsultationRoom consultationRoom, int queueNumber);
    default AppointmentToken getPreviousToken(ConsultationRoom consultationRoom, int queueNumber) {
        return findLastByConsultationRoomAndQueueNumberLessThanOrderByQueueNumber(consultationRoom, queueNumber);
    }

    AppointmentToken findLastByAppointmentProviderAndConsultationRoomAndQueueNumberLessThanOrderByQueueNumber(User user, ConsultationRoom consultationRoom, int queueNumber);
    default AppointmentToken getPreviousToken(ConsultationRoom consultationRoom, int queueNumber, User user) {
        return findLastByAppointmentProviderAndConsultationRoomAndQueueNumberLessThanOrderByQueueNumber(user, consultationRoom, queueNumber);
    }

    AppointmentToken findTopByConsultationRoomOrderByQueueNumberDesc(ConsultationRoom consultationRoom);
    default int getMostRecentAppointmentQueueNumber(ConsultationRoom consultationRoom) {
        AppointmentToken appointmentToken = this.findTopByConsultationRoomOrderByQueueNumberDesc(consultationRoom);
        return appointmentToken == null ? 0 : appointmentToken.getQueueNumber();
    }
}
