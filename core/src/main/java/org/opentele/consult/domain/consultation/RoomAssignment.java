package org.opentele.consult.domain.consultation;

import com.sun.istack.NotNull;
import org.opentele.consult.domain.facility.VirtualRoom;
import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;

@Entity
@Table(name = "room_assignment")
public class RoomAssignment extends OrganisationalEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "primary_room_id")
    @NotNull
    private VirtualRoom primaryRoom;
}
