package org.opentele.consult.domain.consultationRoom;

import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "consultation_room_schedule")
public class ConsultationRoomSchedule extends OrganisationalEntity {
    @Column
    private String title;

    @Column(name = "start_time", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date startTime;

    @Column(name = "end_time", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date endTime;

    @Column(name = "recurrence_rule")
    private String recurrenceRule;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "consultationRoomSchedule")
    private Set<ConsultationRoomScheduleUser> providers = new HashSet<>();
}
