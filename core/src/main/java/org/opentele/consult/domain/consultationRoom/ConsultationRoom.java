package org.opentele.consult.domain.consultationRoom;

import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.framework.OrganisationalEntity;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.domain.teleconference.TeleConference;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "consultationRoom")
    private Set<ConsultationRoomUser> providers = new HashSet<>();

    @ManyToOne(targetEntity = ConsultationRoomSchedule.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_room_schedule_id", columnDefinition = "integer null")
    private ConsultationRoomSchedule consultationRoomSchedule;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "consultationRoom")
    private Set<Appointment> appointments = new HashSet<>();

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "consultationRoom")
    private Set<TeleConference> teleConferences = new HashSet<>();

    @Column(columnDefinition = "int4 check (total_slots > 0)")
    private int totalSlots;

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

    public void setConsultationRoomSchedule(ConsultationRoomSchedule consultationRoomSchedule) {
        this.consultationRoomSchedule = consultationRoomSchedule;
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public List<Appointment> getAppointmentInOrder() {
        return this.getSortedAppointments().collect(Collectors.toList());
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public void addProvider(ConsultationRoomUser consultationRoomUser) {
        this.providers.add(consultationRoomUser);
        consultationRoomUser.setConsultationRoom(this);
    }

    public void removeProvider(ConsultationRoomUser consultationRoomUser) {
        this.providers.remove(consultationRoomUser);
    }

    public boolean isProvider(User user) {
        return this.providers.stream().anyMatch(consultationRoomUser -> consultationRoomUser.getUser().equals(user));
    }

    public int getNumberOfClients() {
        return this.getAppointments().size();
    }

    public int getNumberOfPendingClients() {
        return (int) this.appointments.stream().filter(appointment -> appointment.getConsultation() == null).count();
    }

    public int getNumberOfPendingUserClients(User user) {
        return (int) this.appointments.stream().filter(appointment -> appointment.getConsultation() == null && appointment.getAppointmentProvider().equals(user)).count();
    }

    public int getNumberOfClients(User user) {
        return (int) this.appointments.stream().filter(appointment -> appointment.getAppointmentProvider().equals(user)).count();
    }

    public TeleConference getActiveTeleConference() {
        return this.teleConferences.stream().filter(teleConference -> !teleConference.getInactive()).findFirst().orElse(null);
    }

    public void addTeleConference(TeleConference teleConference) {
        this.teleConferences.add(teleConference);
        teleConference.setConsultationRoom(this);
    }

    public Appointment getFirstAppointment() {
        return getSortedAppointments().findFirst().orElse(null);
    }

    private Stream<Appointment> getSortedAppointments() {
        return this.appointments.stream().sorted(Comparator.comparingInt(Appointment::getQueueNumber));
    }

    public Appointment getCurrentAppointment() {
        return this.appointments.stream().filter(Appointment::isCurrent).findFirst().orElse(null);
    }

    public int getCurrentQueueNumber() {
        Appointment currentAppointment = getCurrentAppointment();
        return currentAppointment.getQueueNumber();
    }

    public Appointment getAppointment(long id) {
        return this.appointments.stream().filter(appointment -> appointment.getId().equals(id)).findFirst().orElse(null);
    }

    public Object getProviderIds() {
        return null;
    }

    public Appointment removeAppointFor(Client client) {
        Appointment appointment = this.appointments.stream().filter(ap -> ap.getClient().equals(client)).findFirst().orElse(null);
        if (appointment != null) {
            this.appointments.remove(appointment);
        }
        return appointment;
    }

    public static class ConsultationRoomCurrentUserSummary {
        private int numberOfClients;

        public int getNumberOfClients() {
            return numberOfClients;
        }

        public void setNumberOfClients(int numberOfClients) {
            this.numberOfClients = numberOfClients;
        }
    }
}
