package org.opentele.consult.contract.security;

import org.opentele.consult.domain.Organisation;

public class OrganisationAndUserCreateRequest extends CurrentUserResponse {
    private String password;
    private String formIoProjectId;
    private Organisation.FormUsageType formUsageType;

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

    public Organisation.FormUsageType getFormUsageType() {
        return formUsageType;
    }

    public void setFormUsageType(Organisation.FormUsageType formUsageType) {
        this.formUsageType = formUsageType;
    }
}
