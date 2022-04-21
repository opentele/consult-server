package org.opentele.consult.controller;

import org.opentele.consult.contract.client.ConsultationSessionRecordContract;
import org.opentele.consult.contract.client.ConsultationSessionRecordResponse;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.client.ConsultationSessionRecord;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.repository.ClientRepository;
import org.opentele.consult.repository.ConsultationRoomRepository;
import org.opentele.consult.repository.ConsultationSessionRecordRepository;
import org.opentele.consult.repository.framework.Repository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasRole('User')")
public class ConsultationSessionRecordController extends BaseController {
    private final ConsultationSessionRecordRepository consultationSessionRecordRepository;
    private final ClientRepository clientRepository;
    private final ConsultationRoomRepository consultationRoomRepository;

    @Autowired
    public ConsultationSessionRecordController(UserService userService, UserSession userSession, ConsultationSessionRecordRepository consultationSessionRecordRepository, ClientRepository clientRepository, ConsultationRoomRepository consultationRoomRepository) {
        super(userService, userSession);
        this.consultationSessionRecordRepository = consultationSessionRecordRepository;
        this.clientRepository = clientRepository;
        this.consultationRoomRepository = consultationRoomRepository;
    }

    @GetMapping("/api/consultationSessionRecord/{id}")
    public ConsultationSessionRecordResponse getConsultationSessionRecord(@PathVariable("id") int id) {
        return ConsultationSessionRecordResponse.from(consultationSessionRecordRepository.findEntity(id, getCurrentOrganisation()));
    }

    @RequestMapping(value = "/api/consultationSessionRecords", method = {RequestMethod.PUT})
    public void save(@RequestBody List<ConsultationSessionRecordContract> contracts) {
        contracts.forEach(this::save);
    }

    @Transactional
    @RequestMapping(value = "/api/consultationSessionRecord", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<Integer> save(@RequestBody ConsultationSessionRecordContract contract) {
        ConsultationSessionRecord entity = Repository.findByIdOrCreate(contract.getId(), getCurrentOrganisation(), consultationSessionRecordRepository, new ConsultationSessionRecord());
        entity.setComplaints(contract.getComplaints());
        entity.setObservations(contract.getObservations());
        entity.setRecommendations(contract.getRecommendations());
        entity.setKeyInference(contract.getKeyInference());
        if (contract.getConsultationRoomId() > 0)
            entity.setConsultationRoom(consultationRoomRepository.findEntity(contract.getConsultationRoomId(), getCurrentOrganisation()));
        if (entity.isNew()) {
            entity.setOrganisation(getCurrentOrganisation());
            Client client = clientRepository.findEntity(contract.getClientId(), getCurrentOrganisation());
            client.addConsultationSessionRecord(entity);
            clientRepository.save(client);
        } else {
            consultationSessionRecordRepository.save(entity);
        }
        return new ResponseEntity<>(entity.getId(), HttpStatus.OK);
    }
}
