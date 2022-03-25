package org.opentele.consult.contract.appointment;

import org.opentele.consult.contract.framework.BaseEntityContract;

public class AppointmentContract extends BaseEntityContract {
    private int queueNumber;
    private int consultationRoomId;
    private int clientId;

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
}
