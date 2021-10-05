package org.opentele.consult.domain.client;

import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;

@Entity
@Table(name = "client")
public class Client extends OrganisationalEntity {
    private String name;
}
