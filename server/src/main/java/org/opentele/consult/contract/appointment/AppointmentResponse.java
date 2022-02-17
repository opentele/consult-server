package org.opentele.consult.contract.appointment;

import org.opentele.consult.domain.consultationRoom.AppointmentToken;

public class AppointmentResponse extends AppointmentRequest {
    public AppointmentResponse() {
    }

    public static AppointmentResponse fromEntity(AppointmentToken appointmentToken) {
        AppointmentResponse appointmentResponse = new AppointmentResponse();
        appointmentResponse.setQueueNumber(appointmentToken.getQueueNumber());
        appointmentResponse.setClientId(appointmentToken.getClient().getId());
        appointmentResponse.setConsultationRoomId(appointmentToken.getConsultationRoom().getId());
        appointmentResponse.setId(appointmentToken.getId());
        return appointmentResponse;
    }
}
