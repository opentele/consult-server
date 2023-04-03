package org.opentele.consult.domain;

import org.opentele.consult.domain.framework.AbstractAuditableEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "organisation")
public class Organisation extends AbstractAuditableEntity {
    @Column(nullable = false)
    private String name;

    @Column
    private String formIoProjectId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormIoProjectId() {
        return formIoProjectId;
    }

    public void setFormIoProjectId(String formIoProjectId) {
        this.formIoProjectId = formIoProjectId;
    }
}
