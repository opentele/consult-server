package org.opentele.consult.domain.facility;

import com.sun.istack.NotNull;
import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;

@Entity
@Table(name = "consultation_schedule")
public class ConsultationSchedule extends OrganisationalEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "virtual_room_id")
    @NotNull
    private VirtualRoom virtualRoom;

    @Column(name = "start_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date startTime;

    @Column(name = "end_time", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date endTime;
}
