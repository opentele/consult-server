package org.opentele.consult.domain.consultationRoom;

import org.opentele.consult.domain.framework.OrganisationalEntity;
import org.opentele.consult.domain.security.User;

import javax.persistence.*;

@Entity
@Table(name = "consultant_room_user")
public class ConsultationRoomUser extends OrganisationalEntity {
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "integer not null")
    private User user;

    @ManyToOne(targetEntity = ConsultationRoom.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_room_id", columnDefinition = "integer not null")
    private ConsultationRoom consultationRoom;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ConsultationRoom getConsultationRoom() {
        return consultationRoom;
    }

    public void setConsultationRoom(ConsultationRoom consultationRoom) {
        this.consultationRoom = consultationRoom;
    }
}
