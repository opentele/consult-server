package org.opentele.consult.contract.appointment;

import org.opentele.consult.contract.framework.BaseEntityContract;
import org.opentele.consult.domain.consultationRoom.Appointment;

public class AppointmentContract extends BaseEntityContract {
    private int queueNumber;
    private int consultationRoomId;
    private int clientId;
    private boolean isCurrent;

    public int getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }

    public int getConsultationRoomId() {
        return consultationRoomId;
    }

    public void setConsultationRoomId(int consultationRoomId) {
        this.consultationRoomId = consultationRoomId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public static AppointmentContract fromEntity(Appointment appointment) {
        AppointmentContract appointmentResponse = new AppointmentContract();
        set(appointment, appointmentResponse);
        return appointmentResponse;
    }

    public static void set(Appointment appointment, AppointmentContract appointmentContract) {
        appointmentContract.setQueueNumber(appointment.getQueueNumber());
        appointmentContract.setClientId(appointment.getClient().getId());
        appointmentContract.setConsultationRoomId(appointment.getConsultationRoom().getId());
        appointmentContract.setId(appointment.getId());
        appointmentContract.setCurrent(appointment.isCurrent());
    }
}
