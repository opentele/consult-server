package org.opentele.consult.repository;

import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.repository.framework.AbstractRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends AbstractRepository<Client> {
    List<Client> findTop10ByNameContainingAndOrganisationOrderByName(String s, Organisation organisation);
}
