package org.opentele.consult.domain.client;

import org.opentele.consult.domain.consultationRoom.AppointmentToken;
import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;

@Table(name = "consultation")
@Entity
public class Consultation extends OrganisationalEntity {
    @ManyToOne(targetEntity = AppointmentToken.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_token_id", columnDefinition = "integer null")
    private AppointmentToken appointmentToken;
}
