package org.opentele.consult.domain;

import org.opentele.consult.domain.framework.AbstractAuditableEntity;

import javax.persistence.*;

@Entity
@Table(name = "organisation")
public class Organisation extends AbstractAuditableEntity {
    @Column(nullable = false)
    private String name;

    @Column
    private String formIoProjectId;

    @Column(columnDefinition = "varchar(10) not null default 'Native'")
    @Enumerated(EnumType.STRING)
    private FormUsageType formUsageType;

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

    public FormUsageType getFormUsageType() {
        return formUsageType;
    }

    public void setFormUsageType(FormUsageType formUsageType) {
        this.formUsageType = formUsageType;
    }

    public static enum FormUsageType {
        Native, FormIO, Both
    }
}
