package org.opentele.consult.domain.consultationRoom;

import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "appointment_token")
public class AppointmentToken extends OrganisationalEntity {
    @ManyToOne(targetEntity = ConsultationRoom.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_room_id", columnDefinition = "integer not null")
    private ConsultationRoom consultationRoom;

    @Column(nullable = false)
    private int queueNumber;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", columnDefinition = "integer not null")
    private Client client;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "appointmentToken")
    private Set<AppointmentTokenUser> appointmentTokenUsers = new HashSet<>();

    public ConsultationRoom getConsultationRoom() {
        return consultationRoom;
    }

    public void setConsultationRoom(ConsultationRoom consultationRoom) {
        this.consultationRoom = consultationRoom;
    }

    public int getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<AppointmentTokenUser> getAppointmentTokenUsers() {
        return appointmentTokenUsers;
    }

    public void setAppointmentTokenUsers(Set<AppointmentTokenUser> appointmentTokenUsers) {
        this.appointmentTokenUsers = appointmentTokenUsers;
    }
}
