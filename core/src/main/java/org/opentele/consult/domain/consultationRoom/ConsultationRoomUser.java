package org.opentele.consult.domain.consultationRoom;

import com.sun.istack.NotNull;
import org.opentele.consult.domain.framework.OrganisationalEntity;
import org.opentele.consult.domain.security.User;

import javax.persistence.*;

@Entity
@Table(name = "consultant_room_user")
public class ConsultationRoomUser extends OrganisationalEntity {
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(targetEntity = ConsultationRoom.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_room_id")
    @NotNull
    private ConsultationRoom consultationRoom;
}