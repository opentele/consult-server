package org.opentele.consult.contract.security;

import org.opentele.consult.domain.Organisation;

public class OrganisationAndUserCreateRequest extends OrganisationUserContract {
    private String password;
    private String formIoProjectId;
    private Organisation.FormUsageType formUsageMode;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
