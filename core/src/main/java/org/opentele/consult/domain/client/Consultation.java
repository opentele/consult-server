package org.opentele.consult.domain.client;

import org.opentele.consult.domain.consultationRoom.Appointment;
import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;

@Table(name = "consultation")
@Entity
public class Consultation extends OrganisationalEntity {
    @ManyToOne(targetEntity = Appointment.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id", columnDefinition = "integer null")
    private Appointment appointment;
}
