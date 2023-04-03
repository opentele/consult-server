package org.opentele.consult.contract.client;

public class ConsultationSessionFormRequest {
    private long id;
    private String formId;
    private String data;
    private long clientId;
    private long consultationRoomId;

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public long getConsultationRoomId() {
        return consultationRoomId;
    }

    public void setConsultationRoomId(long consultationRoomId) {
        this.consultationRoomId = consultationRoomId;
    }
}
