package org.opentele.consult.contract.client;

import java.util.List;

public class ConsultationSessionFormRecordRequest {
    private long id;
    private long clientId;
    private long consultationRoomId;
    private List<FormRecord> formRecords;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<FormRecord> getFormRecords() {
        return formRecords;
    }

    public void setFormRecords(List<FormRecord> formRecords) {
        this.formRecords = formRecords;
    }

    public static class FormRecord {
        private String formId;
        private String data;

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
    }
}
