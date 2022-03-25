package org.opentele.consult.contract.consultationRoom;

import org.opentele.consult.contract.appointment.AppointmentDetailResponse;

import java.util.ArrayList;
import java.util.List;

public class ConsultationRoomConferenceResponse extends ConsultationRoomDetailResponse {
    private List<AppointmentDetailResponse> appointments = new ArrayList<>();

    public List<AppointmentDetailResponse> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentDetailResponse> appointments) {
        this.appointments = appointments;
    }
}
