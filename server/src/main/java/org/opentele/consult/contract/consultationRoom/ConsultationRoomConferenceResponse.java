package org.opentele.consult.contract.consultationRoom;

import org.opentele.consult.contract.appointment.AppointmentResponse;

import java.util.ArrayList;
import java.util.List;

public class ConsultationRoomConferenceResponse extends ConsultationRoomDetailResponse {
    private List<AppointmentResponse> appointments = new ArrayList<>();

    public List<AppointmentResponse> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentResponse> appointments) {
        this.appointments = appointments;
    }
}
