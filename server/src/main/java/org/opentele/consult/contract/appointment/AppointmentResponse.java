package org.opentele.consult.contract.appointment;

import org.opentele.consult.domain.consultationRoom.AppointmentToken;

public class AppointmentResponse extends AppointmentContract {
    public AppointmentResponse() {
    }

    public static AppointmentResponse fromEntity(AppointmentToken appointmentToken) {
        AppointmentResponse appointmentResponse = new AppointmentResponse();
        set(appointmentToken, appointmentResponse);
        return appointmentResponse;
    }

    public static void set(AppointmentToken appointmentToken, AppointmentResponse appointmentResponse) {
        appointmentResponse.setQueueNumber(appointmentToken.getQueueNumber());
        appointmentResponse.setClientId(appointmentToken.getClient().getId());
        appointmentResponse.setConsultationRoomId(appointmentToken.getConsultationRoom().getId());
        appointmentResponse.setId(appointmentToken.getId());
    }
}
