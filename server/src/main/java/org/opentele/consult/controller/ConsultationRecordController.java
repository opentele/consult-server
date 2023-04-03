package org.opentele.consult.controller;

import org.opentele.consult.config.ApplicationConfig;
import org.opentele.consult.contract.client.ConsultationSessionFormRequest;
import org.opentele.consult.contract.client.ConsultationSessionRecordFileContract;
import org.opentele.consult.contract.client.ConsultationSessionRecordRequest;
import org.opentele.consult.contract.client.ConsultationSessionRecordResponse;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.client.ConsultationFormRecord;
import org.opentele.consult.domain.client.ConsultationRecord;
import org.opentele.consult.domain.framework.ConsultationSessionRecordFile;
import org.opentele.consult.framework.FileUtil;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.repository.ClientRepository;
import org.opentele.consult.repository.ConsultationFormRecordRepository;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('User')")
public class ConsultationRecordController extends BaseController {
    private final ConsultationSessionRecordRepository consultationSessionRecordRepository;
    private final ClientRepository clientRepository;
    private final ConsultationRoomRepository consultationRoomRepository;
    private final FileUtil fileUtil;
    private final ApplicationConfig applicationConfig;
    private final ConsultationFormRecordRepository consultationFormRecordRepository;

    private static final String ConsultationRecordBaseEndpoint = "/api/consultationRecord";

    @Autowired
    public ConsultationRecordController(UserService userService, UserSession userSession, ConsultationSessionRecordRepository consultationSessionRecordRepository, ClientRepository clientRepository, ConsultationRoomRepository consultationRoomRepository, FileUtil fileUtil, ApplicationConfig applicationConfig, ConsultationFormRecordRepository consultationFormRecordRepository) {
        super(userService, userSession);
        this.consultationSessionRecordRepository = consultationSessionRecordRepository;
        this.clientRepository = clientRepository;
        this.consultationRoomRepository = consultationRoomRepository;
        this.fileUtil = fileUtil;
        this.applicationConfig = applicationConfig;
        this.consultationFormRecordRepository = consultationFormRecordRepository;
    }

    @GetMapping(ConsultationRecordBaseEndpoint + "/{id}")
    public ConsultationSessionRecordResponse getConsultationSessionRecord(@PathVariable("id") long id) {
        return ConsultationSessionRecordResponse.from(consultationSessionRecordRepository.findEntity(id, getCurrentOrganisation()));
    }

    @GetMapping(ConsultationRecordBaseEndpoint + "/{id}/files")
    public List<ConsultationSessionRecordFileContract> getConsultationSessionRecordFiles(@PathVariable("id") long id) {
        ConsultationRecord consultationSessionRecord = consultationSessionRecordRepository.findEntity(id, getCurrentOrganisation());
        return ConsultationSessionRecordFileContract.from(consultationSessionRecord.getFiles());
    }

    @RequestMapping(value = "/api/consultationRecords", method = {RequestMethod.PUT})
    public void save(@RequestBody List<ConsultationSessionRecordRequest> requests) throws IOException {
        for (ConsultationSessionRecordRequest request : requests) {
            save(request);
        }
    }

    @Transactional
    @RequestMapping(value = ConsultationRecordBaseEndpoint, method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity save(@RequestBody ConsultationSessionRecordRequest request) throws IOException {
        ConsultationRecord entity = Repository.findByIdOrCreate(request.getId(), getCurrentOrganisation(), consultationSessionRecordRepository, new ConsultationRecord());
        entity.setComplaints(request.getComplaints());
        entity.setObservations(request.getObservations());
        entity.setRecommendations(request.getRecommendations());
        entity.setKeyInference(request.getKeyInference());

        List<ConsultationSessionRecordFileContract> newFiles = request.getFiles().stream().filter(x -> x.getId() == 0).collect(Collectors.toList());
        for (ConsultationSessionRecordFileContract fileRequest : newFiles) {
            ConsultationSessionRecordFile file = new ConsultationSessionRecordFile();
            file.setName(fileRequest.getName());
            file.setFileName(fileRequest.getFileName());
            String mimeType = fileUtil.getMimeType(applicationConfig.getAttachmentsLocation(), fileRequest.getFileName());
            file.setMimeType(mimeType);
            file.setOrganisation(getCurrentOrganisation());
            entity.addFile(file);
        }

        if (request.getConsultationRoomId() > 0)
            entity.setConsultationRoom(consultationRoomRepository.findEntity(request.getConsultationRoomId(), getCurrentOrganisation()));
        if (entity.isNew()) {
            entity.setOrganisation(getCurrentOrganisation());
            Client client = clientRepository.findEntity(request.getClientId(), getCurrentOrganisation());
            client.addConsultationRecord(entity);
            clientRepository.save(client);
        } else {
            consultationSessionRecordRepository.save(entity);
        }
        return new ResponseEntity<>(entity.getId(), HttpStatus.OK);
    }

    @Transactional
    @RequestMapping(value = ConsultationRecordBaseEndpoint +  "/form", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity saveForm(@RequestBody ConsultationSessionFormRequest request) {
        ConsultationFormRecord consultationFormRecord = new ConsultationFormRecord();
        consultationFormRecord.setFormId(request.getFormId());
        consultationFormRecord.setData(request.getData());

        if (request.getConsultationRoomId() > 0)
            consultationFormRecord.setConsultationRoom(consultationRoomRepository.findEntity(request.getConsultationRoomId(), getCurrentOrganisation()));
        if (consultationFormRecord.isNew()) {
            consultationFormRecord.setOrganisation(getCurrentOrganisation());
            Client client = clientRepository.findEntity(request.getClientId(), getCurrentOrganisation());
            client.addConsultationFormRecord(consultationFormRecord);
            clientRepository.save(client);
        } else {
            consultationFormRecordRepository.save(consultationFormRecord);
        }

        return new ResponseEntity<>(consultationFormRecord.getId(), HttpStatus.OK);
    }
}
