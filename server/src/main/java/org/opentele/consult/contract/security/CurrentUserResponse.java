package org.opentele.consult.contract.security;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.OrganisationUser;

public class CurrentUserResponse extends OrganisationUserResponse {
    private String organisationName;
    private Organisation.FormUsageType formUsageType;
    private String formIoProjectId;

    public String getFormIoProjectId() {
        return formIoProjectId;
    }
    public String getOrganisationName() {
        return organisationName;
    }

    public Organisation.FormUsageType getFormUsageType() {
        return formUsageType;
    }

    public static OrganisationUserResponse from(OrganisationUser organisationUser) {
        CurrentUserResponse response = new CurrentUserResponse();
        response.populate(organisationUser);

        Organisation organisation = organisationUser.getOrganisation();
        response.organisationName = organisation.getName();
        response.formIoProjectId = organisation.getFormIoProjectId();
        response.formUsageType = organisation.getFormUsageType();
        return response;
    }
}
