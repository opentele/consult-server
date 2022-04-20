package org.opentele.consult.controller;

import org.opentele.consult.contract.client.ClientContract;
import org.opentele.consult.contract.client.ClientSearchResponse;
import org.opentele.consult.contract.client.ClientSearchResults;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.repository.ClientRepository;
import org.opentele.consult.repository.framework.Repository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('User')")
public class ClientController extends BaseController {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository, UserService userService, UserSession userSession) {
        super(userService, userSession);
        this.clientRepository = clientRepository;
    }

    @RequestMapping(value = "/api/clients", method = {RequestMethod.POST, RequestMethod.PUT})
    public void createUpdate(@RequestBody List<ClientContract> listContract) {
        listContract.forEach(this::createUpdate);
    }

    @RequestMapping(value = "/api/client", method = {RequestMethod.POST, RequestMethod.PUT})
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

    @GetMapping("/api/client/findBy")
    public ClientSearchResults getClients(@RequestParam("name") String name, @RequestParam("registrationNumber") String registrationNumber) {
        int count = clientRepository.countAllByOrganisation(getCurrentOrganisation());
        List<ClientSearchResponse> clientSearchResponses = clientRepository.getClientsMatching(name, registrationNumber, getCurrentOrganisation()).stream().map(ClientSearchResponse::from).collect(Collectors.toList());
        return new ClientSearchResults(count, clientSearchResponses);
    }

    @GetMapping("/api/client/search")
    public ClientSearchResults searchClients(@RequestParam("q") String q) {
        int count = clientRepository.countAllByOrganisation(getCurrentOrganisation());
        List<ClientSearchResponse> clientSearchResponses = clientRepository.searchClients(q, getCurrentOrganisation()).stream().map(ClientSearchResponse::from).collect(Collectors.toList());
        return new ClientSearchResults(count, clientSearchResponses);
    }

    @GetMapping("/api/client/{id}")
    public ClientContract get(@PathVariable("id") int id) {
        Client client = clientRepository.findEntity(id, getCurrentOrganisation());
        return ClientContract.from(client);
    }

    @GetMapping("/api/client/{id}/full")
    public ClientContract getFull(@PathVariable("id") int id) {
        Client client = clientRepository.findEntity(id, getCurrentOrganisation());
        return ClientContract.fromWithChildren(client);
    }
}
