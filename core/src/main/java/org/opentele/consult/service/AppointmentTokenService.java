package org.opentele.consult.service;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.consultationRoom.AppointmentToken;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.security.ProviderType;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.repository.AppointmentTokenRepository;
import org.springframework.stereotype.Service;

@Service
public class AppointmentTokenService {
    private final AppointmentTokenRepository appointmentTokenRepository;

    public AppointmentTokenService(AppointmentTokenRepository appointmentTokenRepository) {
        this.appointmentTokenRepository = appointmentTokenRepository;
    }

    public AppointmentToken getNextToken(User user, Organisation organisation, ConsultationRoom consultationRoom) {
        AppointmentToken currentAppointmentToken = consultationRoom.getCurrentAppointmentToken();
        return getNextToken(user, organisation, consultationRoom, currentAppointmentToken);
    }

    public AppointmentToken getNextToken(User user, Organisation organisation, ConsultationRoom consultationRoom, AppointmentToken appointmentToken) {
        ProviderType providerType = user.getProviderType(organisation);
        AppointmentToken nextToken;
        if (providerType.equals(ProviderType.Usher))
            nextToken = appointmentTokenRepository.getNextToken(consultationRoom, appointmentToken.getQueueNumber(), user);
        else
            nextToken = appointmentTokenRepository.getNextToken(consultationRoom, appointmentToken.getQueueNumber());
        return nextToken;
    }

    public AppointmentToken getPreviousToken(User user, Organisation organisation, ConsultationRoom consultationRoom) {
        AppointmentToken currentAppointmentToken = consultationRoom.getCurrentAppointmentToken();
        return getPreviousToken(user, organisation, consultationRoom, currentAppointmentToken);
    }

    public AppointmentToken getPreviousToken(User user, Organisation organisation, ConsultationRoom consultationRoom, AppointmentToken appointmentToken) {
        AppointmentToken previousToken;
        ProviderType providerType = user.getProviderType(organisation);
        if (providerType.equals(ProviderType.Usher))
            previousToken = appointmentTokenRepository.getPreviousToken(consultationRoom, appointmentToken.getQueueNumber(), user);
        else
            previousToken = appointmentTokenRepository.getPreviousToken(consultationRoom, appointmentToken.getQueueNumber());
        return previousToken;
    }
}
