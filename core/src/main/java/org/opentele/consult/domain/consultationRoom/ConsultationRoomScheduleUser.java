package org.opentele.consult.domain.consultationRoom;

import com.sun.istack.NotNull;
import org.opentele.consult.domain.framework.OrganisationalEntity;
import org.opentele.consult.domain.security.User;

import javax.persistence.*;

@Entity
@Table(name = "consultant_room_schedule_user")
public class ConsultationRoomScheduleUser extends OrganisationalEntity {
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(targetEntity = ConsultationRoomSchedule.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_room_schedule_id")
    @NotNull
    private ConsultationRoomSchedule consultationRoomSchedule;

    public void setUser(User user) {
        this.user = user;
    }

    public void setConsultationRoomSchedule(ConsultationRoomSchedule consultationRoomSchedule) {
        this.consultationRoomSchedule = consultationRoomSchedule;
    }
}
