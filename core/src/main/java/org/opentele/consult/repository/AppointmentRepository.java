package org.opentele.consult.repository;

import org.opentele.consult.domain.consultationRoom.Appointment;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends AbstractRepository<Appointment> {
    Appointment findFirstByConsultationRoomAndQueueNumberGreaterThanOrderByQueueNumber(ConsultationRoom consultationRoom, int queueNumber);
    default Appointment getNextToken(ConsultationRoom consultationRoom, int queueNumber) {
        return findFirstByConsultationRoomAndQueueNumberGreaterThanOrderByQueueNumber(consultationRoom, queueNumber);
    }

    Appointment findFirstByAppointmentProviderAndConsultationRoomAndQueueNumberGreaterThanOrderByQueueNumber(User user, ConsultationRoom consultationRoom, int queueNumber);
    default Appointment getNextToken(ConsultationRoom consultationRoom, int queueNumber, User user) {
        return findFirstByAppointmentProviderAndConsultationRoomAndQueueNumberGreaterThanOrderByQueueNumber(user, consultationRoom, queueNumber);
    }

    Appointment findFirstByConsultationRoomAndQueueNumberLessThanOrderByQueueNumberDesc(ConsultationRoom consultationRoom, int queueNumber);
    default Appointment getPreviousToken(ConsultationRoom consultationRoom, int queueNumber) {
        return findFirstByConsultationRoomAndQueueNumberLessThanOrderByQueueNumberDesc(consultationRoom, queueNumber);
    }

    Appointment findLastByAppointmentProviderAndConsultationRoomAndQueueNumberLessThanOrderByQueueNumber(User user, ConsultationRoom consultationRoom, int queueNumber);
    default Appointment getPreviousToken(ConsultationRoom consultationRoom, int queueNumber, User user) {
        return findLastByAppointmentProviderAndConsultationRoomAndQueueNumberLessThanOrderByQueueNumber(user, consultationRoom, queueNumber);
    }

    Appointment findTopByConsultationRoomOrderByQueueNumberDesc(ConsultationRoom consultationRoom);
    default int getMostRecentAppointmentQueueNumber(ConsultationRoom consultationRoom) {
        Appointment appointment = this.findTopByConsultationRoomOrderByQueueNumberDesc(consultationRoom);
        return appointment == null ? 0 : appointment.getQueueNumber();
    }
}
