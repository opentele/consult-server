package org.opentele.consult.contract.security;

import org.opentele.consult.domain.Organisation;

public class OrganisationCreateRequest {
    private String name;
    private String formIoProjectId;
    private Organisation.FormUsageType formUsageMode;

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

    public Organisation.FormUsageType getFormUsageMode() {
        return formUsageMode;
    }

    public void setFormUsageMode(Organisation.FormUsageType formUsageMode) {
        this.formUsageMode = formUsageMode;
    }
}
