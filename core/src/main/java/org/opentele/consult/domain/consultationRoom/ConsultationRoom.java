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
    @Column
    private String title;

    @Column
    private LocalDateTime scheduledStartAt;

    @Column
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
}
