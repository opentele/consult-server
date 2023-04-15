package org.opentele.consult.controller;

import org.opentele.consult.contract.client.*;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.client.ConsultationFormRecord;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.repository.ClientRepository;
import org.opentele.consult.repository.ConsultationFormRecordRepository;
import org.opentele.consult.repository.framework.Repository;
import org.opentele.consult.service.UserService;
import org.opentele.consult.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('User')")
public class ClientController extends BaseController {
    private final ClientRepository clientRepository;
    private static final String ClientEndpoint = "/api/client";

    @Autowired
    public ClientController(ClientRepository clientRepository, UserService userService, UserSession userSession) {
        super(userService, userSession);
        this.clientRepository = clientRepository;
    }

    @RequestMapping(value = ClientEndpoint + "s", method = {RequestMethod.POST, RequestMethod.PUT})
    @Transactional
    public List<ClientContract> createUpdate(@RequestBody List<ClientContract> listContract) {
        return listContract.stream().map(this::createUpdate).collect(Collectors.toList());
    }

    @RequestMapping(value = ClientEndpoint, method = {RequestMethod.POST, RequestMethod.PUT})
    @Transactional
    public ClientContract createUpdate(@RequestBody ClientContract contract) {
        Client client = Repository.findByIdOrCreate(contract.getId(), getCurrentOrganisation(), clientRepository, new Client());
        client.setGender(contract.getGender());
        client.setDateOfBirth(contract.getDateOfBirth());
        client.setName(contract.getName());
        client.setOrganisation(getCurrentOrganisation());
        client.setRegistrationNumber(contract.getRegistrationNumber());
        contract.setId(clientRepository.save(client).getId());
        return contract;
    }

    @GetMapping(ClientEndpoint + "/findBy")
    public ClientSearchResults getClients(@RequestParam("name") String name, @RequestParam("registrationNumber") String registrationNumber) {
        int count = clientRepository.countAllByOrganisation(getCurrentOrganisation());
        List<ClientSearchResponse> clientSearchResponses = clientRepository.getClientsMatching(name, registrationNumber, getCurrentOrganisation()).stream().map(ClientSearchResponse::from).collect(Collectors.toList());
        return new ClientSearchResults(count, clientSearchResponses);
    }

    @GetMapping(ClientEndpoint + "/search")
    public ClientSearchResults searchClients(@RequestParam("q") String q) {
        int count = clientRepository.countAllByOrganisation(getCurrentOrganisation());
        List<ClientSearchResponse> clientSearchResponses = clientRepository.searchClients(q, getCurrentOrganisation()).stream().map(ClientSearchResponse::from).collect(Collectors.toList());
        return new ClientSearchResults(count, clientSearchResponses);
    }

    @GetMapping(ClientEndpoint + "/{id}")
    public ClientNativeSessionRecordsContract get(@PathVariable("id") long id) {
        Client client = clientRepository.findEntity(id, getCurrentOrganisation());
        return ClientNativeSessionRecordsContract.from(client);
    }

    @GetMapping(ClientEndpoint + "/{id}/full")
    public ClientNativeSessionRecordsContract getFull(@PathVariable("id") long id) {
        Client client = clientRepository.findEntity(id, getCurrentOrganisation());
        return ClientNativeSessionRecordsContract.createForSessionRecords(client);
    }
}
