package org.opentele.consult.service;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.security.User;
import org.opentele.consult.repository.OrganisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class OrganisationService {
    private final OrganisationRepository organisationRepository;

    @Autowired
    public OrganisationService(OrganisationRepository organisationRepository) {
        this.organisationRepository = organisationRepository;
    }

    public Organisation createOrg(String organisationName, User user, String formIoProjectId, Organisation.FormUsageType formUsageType) {
        Organisation organisation = new Organisation();
        organisation.setName(organisationName);
        organisation.setFormIoProjectId(formIoProjectId);
        organisation.setCreatedBy(user);
        organisation.setLastModifiedBy(user);
        organisation.setFormUsageType(Objects.requireNonNullElse(formUsageType, Organisation.FormUsageType.Native));
        return organisationRepository.save(organisation);
    }
}
