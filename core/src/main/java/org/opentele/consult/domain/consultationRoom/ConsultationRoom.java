package org.opentele.consult.domain.consultationRoom;

import org.opentele.consult.domain.framework.OrganisationalEntity;
import org.opentele.consult.domain.security.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "consultation_room")
public class ConsultationRoom  extends OrganisationalEntity {
    @Column
    private String title;

    @Column(name = "scheduled_start_time", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date scheduledStartTime;

    @Column(name = "scheduled_end_time", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date scheduledEndTime;

    @Column(name = "start_time", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date startTime;

    @Column(name = "end_time", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date endTime;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "consultationRoom")
    private Set<ConsultationRoomUser> providers = new HashSet<>();

    @ManyToOne(targetEntity = ConsultationRoomSchedule.class, fetch = FetchType.LAZY)
    private ConsultationRoomSchedule consultationRoomSchedule;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "consultationRoom")
    private Set<AppointmentToken> appointmentTokens = new HashSet<>();
}
