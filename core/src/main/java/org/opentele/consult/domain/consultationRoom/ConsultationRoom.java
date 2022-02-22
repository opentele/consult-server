package org.opentele.consult.domain.consultationRoom;

import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.framework.OrganisationalEntity;
import org.opentele.consult.domain.security.User;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "consultation_room")
public class ConsultationRoom  extends OrganisationalEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate scheduledOn;

    @Column(nullable = false)
    private LocalTime scheduledStartTime;

    @Column(nullable = false)
    private LocalTime scheduledEndTime;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "consultationRoom")
    private Set<ConsultationRoomUser> providers = new HashSet<>();

    @ManyToOne(targetEntity = ConsultationRoomSchedule.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_room_schedule_id", columnDefinition = "integer not null")
    private ConsultationRoomSchedule consultationRoomSchedule;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "consultationRoom")
    private Set<AppointmentToken> appointmentTokens = new HashSet<>();

    @Column
    private int totalSlots;

    @Column
    private int currentQueueNumber;

    public LocalDate getScheduledOn() {
        return scheduledOn;
    }

    public void setScheduledOn(LocalDate scheduledOn) {
        this.scheduledOn = scheduledOn;
    }

    public LocalTime getScheduledStartTime() {
        return scheduledStartTime;
    }

    public void setScheduledStartTime(LocalTime scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }

    public LocalTime getScheduledEndTime() {
        return scheduledEndTime;
    }

    public void setScheduledEndTime(LocalTime scheduledEndTime) {
        this.scheduledEndTime = scheduledEndTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public ConsultationRoomSchedule getSchedule() {
        return consultationRoomSchedule;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<ConsultationRoomUser> getProviders() {
        return providers;
    }

    public void setProviders(Set<ConsultationRoomUser> providers) {
        this.providers = providers;
    }

    public void setConsultationRoomSchedule(ConsultationRoomSchedule consultationRoomSchedule) {
        this.consultationRoomSchedule = consultationRoomSchedule;
    }

    public Set<AppointmentToken> getAppointmentTokens() {
        return appointmentTokens;
    }

    public void setAppointmentTokens(Set<AppointmentToken> appointmentTokens) {
        this.appointmentTokens = appointmentTokens;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public void addProvider(ConsultationRoomUser consultationRoomUser) {
        this.providers.add(consultationRoomUser);
    }

    public boolean isProvider(User user) {
        return this.providers.stream().anyMatch(consultationRoomUser -> consultationRoomUser.getUser().equals(user));
    }

    public int getNumberOfClients() {
        return this.getAppointmentTokens().size();
    }

    public int getCurrentQueueNumber() {
        return currentQueueNumber;
    }

    public void setCurrentQueueNumber(int currentQueueNumber) {
        this.currentQueueNumber = currentQueueNumber;
    }

    public int getNumberOfPendingClients() {
        return (int) this.appointmentTokens.stream().filter(appointmentToken -> appointmentToken.getConsultation() == null).count();
    }

    public int getNumberOfPendingUserClients(User user) {
        return (int) this.appointmentTokens.stream().filter(appointmentToken -> appointmentToken.getConsultation() == null && appointmentToken.getAppointmentProvider().equals(user)).count();
    }

    public int getNumberOfClients(User user) {
        return (int) this.appointmentTokens.stream().filter(appointmentToken -> appointmentToken.getAppointmentProvider().equals(user)).count();
    }

    public static class ConsultationRoomCurrentUserSummary {
        private int numberOfClients;
        private Client nextClient;

        public int getNumberOfClients() {
            return numberOfClients;
        }

        public void setNumberOfClients(int numberOfClients) {
            this.numberOfClients = numberOfClients;
        }

        public Client getNextClient() {
            return nextClient;
        }

        public void setNextClient(Client nextClient) {
            this.nextClient = nextClient;
        }
    }
}
