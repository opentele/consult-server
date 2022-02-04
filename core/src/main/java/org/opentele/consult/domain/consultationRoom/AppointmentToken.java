package org.opentele.consult.domain.consultationRoom;

import com.sun.istack.NotNull;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;

@Entity
@Table(name = "appointment_token")
public class AppointmentToken extends OrganisationalEntity {
    @ManyToOne(targetEntity = ConsultationRoomSchedule.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_room_id")
    @NotNull
    private ConsultationRoom consultationRoom;

    @Column(nullable = false)
    private int queueNumber;

    @ManyToOne(targetEntity = Client.class, fetch = FetchType.LAZY)
    private Client client;
}
