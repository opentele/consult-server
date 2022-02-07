package org.opentele.consult.domain.framework;

import com.sun.istack.NotNull;
import org.opentele.consult.domain.Organisation;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class OrganisationalEntity extends AbstractEntity {
    @ManyToOne(targetEntity = Organisation.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "organisation_id")
    @NotNull
    private Organisation organisation;

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }
}
