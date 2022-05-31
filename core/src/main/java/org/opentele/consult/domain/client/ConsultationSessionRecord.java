package org.opentele.consult.domain.client;

import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.framework.ConsultationSessionRecordFile;
import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(targetEntity = ConsultationRoom.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_room_id", columnDefinition = "integer")
    private ConsultationRoom consultationRoom;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "consultationSessionRecord")
    private List<ConsultationSessionRecordFile> files = new ArrayList<>();

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

    public ConsultationRoom getConsultationRoom() {
        return consultationRoom;
    }

    public void setConsultationRoom(ConsultationRoom consultationRoom) {
        this.consultationRoom = consultationRoom;
    }

    public void addFile(ConsultationSessionRecordFile file) {
        files.add(file);
        file.setConsultationSessionRecord(this);
    }
}
