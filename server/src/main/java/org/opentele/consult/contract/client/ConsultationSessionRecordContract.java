package org.opentele.consult.contract.client;

import org.opentele.consult.contract.framework.BaseEntityContract;

public class ConsultationSessionRecordContract extends BaseEntityContract {
    private String complaints;
    private String observations;
    private String keyInference;
    private String recommendations;
    private long clientId;
    private long consultationRoomId;

    public long getClientId() {
        return clientId;
    }

    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    public String getComplaints() {
        return complaints;
    }

    public void setComplaints(String complaints) {
        this.complaints = complaints;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getKeyInference() {
        return keyInference;
    }

    public void setKeyInference(String keyInference) {
        this.keyInference = keyInference;
    }

    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public long getConsultationRoomId() {
        return consultationRoomId;
    }

    public void setConsultationRoomId(long consultationRoomId) {
        this.consultationRoomId = consultationRoomId;
    }
}
