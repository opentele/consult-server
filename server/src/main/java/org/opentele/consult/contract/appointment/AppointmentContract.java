package org.opentele.consult.contract.appointment;

import org.opentele.consult.contract.framework.BaseEntityContract;
import org.opentele.consult.domain.consultationRoom.AppointmentToken;

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

    public static AppointmentContract fromEntity(AppointmentToken appointmentToken) {
        AppointmentContract appointmentResponse = new AppointmentContract();
        set(appointmentToken, appointmentResponse);
        return appointmentResponse;
    }

    public static void set(AppointmentToken appointmentToken, AppointmentContract appointmentContract) {
        appointmentContract.setQueueNumber(appointmentToken.getQueueNumber());
        appointmentContract.setClientId(appointmentToken.getClient().getId());
        appointmentContract.setConsultationRoomId(appointmentToken.getConsultationRoom().getId());
        appointmentContract.setId(appointmentToken.getId());
        appointmentContract.setCurrent(appointmentToken.isCurrent());
    }
}
