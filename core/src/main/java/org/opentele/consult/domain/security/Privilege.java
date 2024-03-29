package org.opentele.consult.domain.security;

import org.opentele.consult.domain.framework.AbstractAuditableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "privilege")
public class Privilege extends AbstractAuditableEntity {
    @Column(name = "name", nullable = false)
    private String name;
}
