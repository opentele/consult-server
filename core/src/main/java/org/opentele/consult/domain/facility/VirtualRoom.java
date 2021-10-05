package org.opentele.consult.domain.facility;

import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "virtual_room")
public class VirtualRoom extends OrganisationalEntity {
    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "virtualRoom")
    private Set<AppointmentToken> appointmentTokens = new HashSet<>();

    @Column
    private String title;
}
