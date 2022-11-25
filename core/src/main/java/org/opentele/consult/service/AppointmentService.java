package org.opentele.consult.service;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.consultationRoom.Appointment;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public Appointment getNextToken(User user, Organisation organisation, ConsultationRoom consultationRoom) {
        Appointment currentAppointment = consultationRoom.getCurrentAppointment();
        return getNextToken(user, organisation, consultationRoom, currentAppointment);
    }

    public Appointment getNextToken(User user, Organisation organisation, ConsultationRoom consultationRoom, Appointment appointment) {
        ProviderType providerType = user.getProviderType(organisation);
        Appointment nextToken;
        if (providerType.equals(ProviderType.Moderator))
            nextToken = appointmentRepository.getNextToken(consultationRoom, appointment.getQueueNumber(), user);
        else
            nextToken = appointmentRepository.getNextToken(consultationRoom, appointment.getQueueNumber());
        return nextToken;
    }

    public Appointment getPreviousToken(User user, Organisation organisation, ConsultationRoom consultationRoom) {
        Appointment currentAppointment = consultationRoom.getCurrentAppointment();
        return getPreviousToken(user, organisation, consultationRoom, currentAppointment);
    }

    public Appointment getPreviousToken(User user, Organisation organisation, ConsultationRoom consultationRoom, Appointment appointment) {
        Appointment previousToken;
        ProviderType providerType = user.getProviderType(organisation);
        if (providerType.equals(ProviderType.Moderator))
            previousToken = appointmentRepository.getPreviousToken(consultationRoom, appointment.getQueueNumber(), user);
        else
            previousToken = appointmentRepository.getPreviousToken(consultationRoom, appointment.getQueueNumber());
        return previousToken;
    }

    public void delete(Appointment appointment) {
        appointmentRepository.delete(appointment);
    }
}
