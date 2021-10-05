package org.opentele.consult.domain.consultation;

import com.sun.istack.NotNull;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.facility.AppointmentToken;
import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;

@Entity
@Table(name = "client_token")
public class ClientToken extends OrganisationalEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    @NotNull
    private Client client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "appointment_token_id")
    @NotNull
    private AppointmentToken appointmentToken;
}
