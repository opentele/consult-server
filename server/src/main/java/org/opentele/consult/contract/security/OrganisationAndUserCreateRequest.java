package org.opentele.consult.contract.security;

public class OrganisationAndUserCreateRequest extends OrganisationUserContract {
    private String password;
    private String formIoProjectId;

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
}
