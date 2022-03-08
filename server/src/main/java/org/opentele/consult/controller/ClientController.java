package org.opentele.consult.controller;

import org.opentele.consult.contract.client.ClientRequestResponse;
import org.opentele.consult.contract.client.ClientSearchResponse;
import org.opentele.consult.contract.client.ConsultationRoomClientResponse;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.repository.ClientRepository;
import org.opentele.consult.repository.framework.Repository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('User')")
public class ClientController {
    private final ClientRepository clientRepository;
    private final UserService userService;
    private UserSession userSession;

    @Autowired
    public ClientController(ClientRepository clientRepository, UserService userService, UserSession userSession) {
        this.clientRepository = clientRepository;
        this.userService = userService;
        this.userSession = userSession;
    }

    @GetMapping("/api/client/search")
    public List<ClientSearchResponse> searchResults(@RequestParam("q") String q,
                                                    @RequestParam("consultationRoomId") int consultationRoomId) {
        return clientRepository.getClientsMatching(q, userSession.getCurrentOrganisation(), consultationRoomId).stream().map(client -> {
            ClientSearchResponse clientSearchResponse = new ClientSearchResponse();
            clientSearchResponse.setId(client.getId());
            clientSearchResponse.setRegistrationNumber(client.getRegistrationNumber());
            clientSearchResponse.setName(client.getName());
            return clientSearchResponse;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/api/clients", method = {RequestMethod.POST, RequestMethod.PUT})
    public void createUpdate(@RequestBody List<ClientRequestResponse> listContract) {
        listContract.forEach(this::createUpdate);
    }

    @GetMapping(value = "/api/client")
    public List<ConsultationRoomClientResponse> getClients(@RequestParam(name = "consultationRoomId") int consultationRoomId) {
        return clientRepository.getClients(consultationRoomId).stream().map(projection -> {
            ConsultationRoomClientResponse response = new ConsultationRoomClientResponse();
            response.setGender(projection.getGender());
            response.setQueueNumber(projection.getQueueNumber());
            response.setAge(Period.between(LocalDate.now(), projection.getDateOfBirth()));
            response.setName(projection.getName());
            response.setRegistrationNumber(projection.getRegistrationNumber());
            return response;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/api/client", method = {RequestMethod.POST, RequestMethod.PUT})
    @Transactional
    public ClientRequestResponse createUpdate(@RequestBody ClientRequestResponse contract) {
        Client client = Repository.findByIdOrCreate(contract.getId(), clientRepository, new Client());
        client.setGender(contract.getGender());
        client.setDateOfBirth(contract.getDateOfBirth());
        client.setName(contract.getName());
        client.setOrganisation(userSession.getCurrentOrganisation());
        client.setRegistrationNumber(contract.getRegistrationNumber());

        contract.setId(clientRepository.save(client).getId());
        return contract;
    }
}
