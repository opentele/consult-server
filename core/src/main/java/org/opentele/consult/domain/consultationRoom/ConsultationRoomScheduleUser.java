package org.opentele.consult.domain.consultationRoom;

import com.sun.istack.NotNull;
import org.opentele.consult.domain.framework.OrganisationalEntity;
import org.opentele.consult.domain.security.User;

import javax.persistence.*;

@Entity
@Table(name = "consultation_room_schedule_user")
public class ConsultationRoomScheduleUser extends OrganisationalEntity {
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "integer not null")
    private User user;

    @ManyToOne(targetEntity = ConsultationRoomSchedule.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_room_schedule_id", columnDefinition = "integer not null")
    @NotNull
    private ConsultationRoomSchedule consultationRoomSchedule;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public ConsultationRoomSchedule getConsultationRoomSchedule() {
        return consultationRoomSchedule;
    }

    public void setConsultationRoomSchedule(ConsultationRoomSchedule consultationRoomSchedule) {
        this.consultationRoomSchedule = consultationRoomSchedule;
    }
}
