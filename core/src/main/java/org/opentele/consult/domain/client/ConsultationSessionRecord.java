package org.opentele.consult.domain.client;

import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;

@Table(name = "consultation_session_record")
@Entity
public class ConsultationSessionRecord extends OrganisationalEntity {
    @Column(columnDefinition = "varchar(10000) not null")
    private String complaints;

    @Column(columnDefinition = "varchar(10000) not null")
    private String observations;

    @Column(columnDefinition = "varchar(10000) not null")
    private String keyInference;

    @Column(columnDefinition = "varchar(10000) not null")
    private String recommendations;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", columnDefinition = "integer not null")
    private Client client;

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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}
