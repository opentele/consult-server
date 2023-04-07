package org.opentele.consult.controller;

import org.opentele.consult.contract.client.*;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.client.ConsultationFormRecord;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.repository.ClientRepository;
import org.opentele.consult.repository.ConsultationFormRecordRepository;
import org.opentele.consult.repository.framework.Repository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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
    private ConsultationFormRecordRepository consultationFormRecordRepository;

    @Autowired
    public ClientController(ClientRepository clientRepository, UserService userService, UserSession userSession, ConsultationFormRecordRepository consultationFormRecordRepository) {
        super(userService, userSession);
        this.clientRepository = clientRepository;
        this.consultationFormRecordRepository = consultationFormRecordRepository;
    }

    @RequestMapping(value = "/api/clients", method = {RequestMethod.POST, RequestMethod.PUT})
    public void createUpdate(@RequestBody List<ClientNativeSessionRecordsResponse> listContract) {
        listContract.forEach(this::createUpdate);
    }

    @RequestMapping(value = "/api/client", method = {RequestMethod.POST, RequestMethod.PUT})
    @Transactional
    public ClientNativeSessionRecordsResponse createUpdate(@RequestBody ClientNativeSessionRecordsResponse contract) {
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
    public ClientNativeSessionRecordsResponse get(@PathVariable("id") long id) {
        Client client = clientRepository.findEntity(id, getCurrentOrganisation());
        return ClientNativeSessionRecordsResponse.from(client);
    }

    @GetMapping("/api/client/{id}/full")
    public ClientNativeSessionRecordsResponse getFull(@PathVariable("id") long id) {
        Client client = clientRepository.findEntity(id, getCurrentOrganisation());
        return ClientNativeSessionRecordsResponse.createForSessionRecords(client);
    }

    @GetMapping("/api/client/{id}/formRecord/recent")
    public ClientFormSessionRecordsResponse getRecentForms(@PathVariable("id") long id) {
        Client client = clientRepository.findEntity(id, getCurrentOrganisation());
        return ClientFormSessionRecordsResponse.createForRecentForms(client);
    }

    @GetMapping("/api/client/formRecord/{id}")
    public ConsultationFormRecordResponse getFormRecord(@PathVariable("id") long id) {
        ConsultationFormRecord entity = consultationFormRecordRepository.findEntity(id, getCurrentOrganisation());
        return ConsultationFormRecordResponse.create(entity);
    }

    //    Grouping by form
    @GetMapping("/api/client/{id}/formRecordSummaryByForm")
    public Map<String, List<FormRecordSummaryResponse>> getFormRecordSummary(@PathVariable long id) {
        Client client = clientRepository.findEntity(id, getCurrentOrganisation());
        Map<String, List<ConsultationFormRecord>> formRecordsGroupedByDate = client.getConsultationFormRecords().stream().collect(Collectors.groupingBy(ConsultationFormRecord::getFormId));
        return formRecordsGroupedByDate.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                localDateListEntry -> localDateListEntry.getValue().stream().map(FormRecordSummaryResponse::new).toList()));
    }

    //    Grouping by date
    @GetMapping("/api/client/{id}/formRecordSummaryByDate")
    public Map<LocalDate, List<FormRecordSummaryResponse>> getFormRecordSummaryByDate(@PathVariable long id) {
        Client client = clientRepository.findEntity(id, getCurrentOrganisation());
        Map<LocalDate, List<ConsultationFormRecord>> formRecordsGroupedByDate = client.getConsultationFormRecords().stream().collect(Collectors.groupingBy(consultationFormRecord -> LocalDate.from(consultationFormRecord.getCreatedDate().toInstant())));
        return formRecordsGroupedByDate.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                localDateListEntry -> localDateListEntry.getValue().stream().map(FormRecordSummaryResponse::new).toList()));
    }
}
