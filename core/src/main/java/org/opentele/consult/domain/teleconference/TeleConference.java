package org.opentele.consult.domain.teleconference;

import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;

@Entity
@Table(name = "tele_conference")
public class TeleConference extends OrganisationalEntity {
    @ManyToOne(targetEntity = ConsultationRoom.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_room_id", columnDefinition = "integer not null")
    private ConsultationRoom consultationRoom;

    @Column
    private String jitsiId;

    public ConsultationRoom getConsultationRoom() {
        return consultationRoom;
    }

    public void setConsultationRoom(ConsultationRoom consultationRoom) {
        this.consultationRoom = consultationRoom;
    }

    public String getJitsiId() {
        return jitsiId;
    }

    public void setJitsiId(String jitsiId) {
        this.jitsiId = jitsiId;
    }
}
