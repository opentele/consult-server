package org.opentele.consult.controller;

import org.opentele.consult.contract.client.ClientRequestResponse;
import org.opentele.consult.contract.client.ClientSearchResponse;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.repository.ClientRepository;
import org.opentele.consult.repository.framework.Repository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('User')")
public class ClientController {
    private final ClientRepository clientRepository;
    private final UserService userService;

    @Autowired
    public ClientController(ClientRepository clientRepository, UserService userService) {
        this.clientRepository = clientRepository;
        this.userService = userService;
    }

    @GetMapping("/api/client/search")
    public List<ClientSearchResponse> searchResults(@RequestParam("q") String q, Principal principal) {
        return clientRepository.findTop10ByNameContainingAndOrganisationOrderByName(q, userService.getOrganisation(principal)).stream().map(client -> {
            ClientSearchResponse clientSearchResponse = new ClientSearchResponse();
            clientSearchResponse.setRegistrationNumber(client.getRegistrationNumber());
            clientSearchResponse.setName(client.getName());
            return clientSearchResponse;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/api/client", method = {RequestMethod.POST, RequestMethod.PUT})
    public ClientRequestResponse createUpdate(@RequestBody ClientRequestResponse contract, Principal principal) {
        Client client = Repository.findByIdOrCreate(contract.getId(), clientRepository, new Client());
        client.setGender(contract.getGender());
        client.setDateOfBirth(contract.getDateOfBirth());
        client.setName(contract.getName());
        client.setOrganisation(userService.getOrganisation(principal));
        client.setRegistrationNumber(contract.getRegistrationNumber());

        contract.setId(clientRepository.save(client).getId());
        return contract;
    }
}
