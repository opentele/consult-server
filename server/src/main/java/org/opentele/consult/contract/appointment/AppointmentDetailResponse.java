package org.opentele.consult.contract.appointment;

import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.client.Gender;
import org.opentele.consult.domain.consultationRoom.Appointment;

public class AppointmentDetailResponse extends AppointmentContract {
    private String clientName;
    private Gender gender;
    private String moderatorName;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getModeratorName() {
        return moderatorName;
    }

    public void setModeratorName(String moderatorName) {
        this.moderatorName = moderatorName;
    }

    public static AppointmentDetailResponse fromEntity(Appointment appointment) {
        AppointmentDetailResponse appointmentDetailResponse = new AppointmentDetailResponse();
        AppointmentContract.set(appointment, appointmentDetailResponse);
        Client client = appointment.getClient();
        appointmentDetailResponse.setClientName(client.getName());
        appointmentDetailResponse.setGender(client.getGender());
        appointmentDetailResponse.setModeratorName(appointment.getAppointmentProvider().getName());
        return appointmentDetailResponse;
    }
}
