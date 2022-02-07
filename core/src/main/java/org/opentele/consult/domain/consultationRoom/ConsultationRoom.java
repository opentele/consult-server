package org.opentele.consult.domain.consultationRoom;

import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "consultation_room")
public class ConsultationRoom  extends OrganisationalEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDateTime scheduledStartAt;

    @Column(nullable = false)
    private LocalDateTime scheduledEndAt;

    @Column
    private LocalTime startedAt;

    @Column
    private LocalTime endedAt;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "consultationRoom")
    private Set<ConsultationRoomUser> providers = new HashSet<>();

    @ManyToOne(targetEntity = ConsultationRoomSchedule.class, fetch = FetchType.LAZY)
    private ConsultationRoomSchedule consultationRoomSchedule;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "consultationRoom")
    private Set<AppointmentToken> appointmentTokens = new HashSet<>();

    @Column
    private int totalSlots;

    public LocalDateTime getScheduledStartAt() {
        return scheduledStartAt;
    }

    public void setScheduledStartAt(LocalDateTime scheduledStartAt) {
        this.scheduledStartAt = scheduledStartAt;
    }

    public LocalDateTime getScheduledEndAt() {
        return scheduledEndAt;
    }

    public void setScheduledEndAt(LocalDateTime scheduledEndAt) {
        this.scheduledEndAt = scheduledEndAt;
    }

    public LocalTime getStartedAt() {
        return startedAt;
    }

    public LocalTime getEndedAt() {
        return endedAt;
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

    public void setStartedAt(LocalTime startedAt) {
        this.startedAt = startedAt;
    }

    public void setEndedAt(LocalTime endedAt) {
        this.endedAt = endedAt;
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
}
