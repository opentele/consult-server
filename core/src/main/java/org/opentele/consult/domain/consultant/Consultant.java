package org.opentele.consult.domain.consultant;

import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;

@Entity
@Table(name = "consultant")
public class Consultant extends OrganisationalEntity {
    @Column
    private String name;
}
