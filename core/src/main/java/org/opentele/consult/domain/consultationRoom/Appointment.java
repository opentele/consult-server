package org.opentele.consult.domain.consultationRoom;

import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.client.Consultation;
import org.opentele.consult.domain.framework.OrganisationalEntity;
import org.opentele.consult.domain.security.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "appointment", uniqueConstraints = {@UniqueConstraint(columnNames = {"consultation_room_id", "client_id"})})
public class Appointment extends OrganisationalEntity {
    @ManyToOne(targetEntity = ConsultationRoom.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_room_id", columnDefinition = "integer not null")
    private ConsultationRoom consultationRoom;

    @Column(nullable = false)
    private int queueNumber;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", columnDefinition = "integer not null")
    private Client client;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_provider_id", columnDefinition = "integer not null")
    private User appointmentProvider;

    @ManyToOne(targetEntity = Consultation.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_id", columnDefinition = "integer null")
    private Consultation consultation;

    @Column
    private boolean isCurrent;

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

    public User getAppointmentProvider() {
        return appointmentProvider;
    }

    public void setAppointmentProvider(User appointmentProvider) {
        this.appointmentProvider = appointmentProvider;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }
}
