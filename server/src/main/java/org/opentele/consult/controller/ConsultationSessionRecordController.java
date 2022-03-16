package org.opentele.consult.controller;

import org.opentele.consult.contract.client.ConsultationSessionRecordContract;
import org.opentele.consult.contract.client.ConsultationSessionRecordResponse;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.client.ConsultationSessionRecord;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.repository.ClientRepository;
import org.opentele.consult.repository.ConsultationSessionRecordRepository;
import org.opentele.consult.repository.framework.Repository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('User')")
public class ConsultationSessionRecordController extends BaseController {
    private final ConsultationSessionRecordRepository consultationSessionRecordRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ConsultationSessionRecordController(UserService userService, UserSession userSession, ConsultationSessionRecordRepository consultationSessionRecordRepository, ClientRepository clientRepository) {
        super(userService, userSession);
        this.consultationSessionRecordRepository = consultationSessionRecordRepository;
        this.clientRepository = clientRepository;
    }

    @GetMapping("/api/consultationSessionRecord")
    public ResponseEntity<ConsultationSessionRecordResponse> getConsultationSessionRecords(@RequestParam(value = "id", required = false) int id,
                                                                                           @RequestParam(value = "clientId", required = false) int clientId) {
        if (id > 0)
            return new ResponseEntity<>(ConsultationSessionRecordResponse.from(consultationSessionRecordRepository.findEntity(id)), HttpStatus.OK);
        if (clientId > 0)
            return new ResponseEntity<>(ConsultationSessionRecordResponse.from(consultationSessionRecordRepository.findByClientId(clientId)), HttpStatus.OK);

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/api/consultationSessionRecord", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Integer> save(@RequestBody ConsultationSessionRecordContract contract) {
        ConsultationSessionRecord entity = Repository.findByIdOrCreate(contract.getId(), consultationSessionRecordRepository, new ConsultationSessionRecord());
        entity.setComplaints(contract.getComplaints());
        entity.setObservations(contract.getObservations());
        entity.setRecommendations(contract.getRecommendations());
        entity.setKeyInference(contract.getKeyInference());
        Client client = clientRepository.findEntity(contract.getClientId());
        client.addConsultationSessionRecord(entity);
        clientRepository.save(client);
        return new ResponseEntity<>(entity.getId(), HttpStatus.OK);
    }
}
