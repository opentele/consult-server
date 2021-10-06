package org.opentele.consult.domain.facility;

import com.sun.istack.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;

@Entity
@Table(name = "appointment_token")
public class AppointmentToken extends OrganisationalEntity {
    @ManyToOne(targetEntity = VirtualRoom.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "virtual_room_id")
    @NotNull
    private VirtualRoom virtualRoom;

    @Column
    private int order;
}
