package org.opentele.consult.controller;

import org.opentele.consult.config.ApplicationConfig;
import org.opentele.consult.contract.client.ConsultationSessionRecordFileContract;
import org.opentele.consult.contract.client.ConsultationSessionRecordRequest;
import org.opentele.consult.contract.client.ConsultationSessionRecordResponse;
import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.client.ConsultationSessionRecord;
import org.opentele.consult.domain.framework.ConsultationSessionRecordFile;
import org.opentele.consult.framework.FileUtil;
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

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('User')")
public class ConsultationSessionRecordController extends BaseController {
    private final ConsultationSessionRecordRepository consultationSessionRecordRepository;
    private final ClientRepository clientRepository;
    private final ConsultationRoomRepository consultationRoomRepository;
    private final FileUtil fileUtil;
    private final ApplicationConfig applicationConfig;

    @Autowired
    public ConsultationSessionRecordController(UserService userService, UserSession userSession, ConsultationSessionRecordRepository consultationSessionRecordRepository, ClientRepository clientRepository, ConsultationRoomRepository consultationRoomRepository, FileUtil fileUtil, ApplicationConfig applicationConfig) {
        super(userService, userSession);
        this.consultationSessionRecordRepository = consultationSessionRecordRepository;
        this.clientRepository = clientRepository;
        this.consultationRoomRepository = consultationRoomRepository;
        this.fileUtil = fileUtil;
        this.applicationConfig = applicationConfig;
    }

    @GetMapping("/api/consultationSessionRecord/{id}")
    public ConsultationSessionRecordResponse getConsultationSessionRecord(@PathVariable("id") int id) {
        return ConsultationSessionRecordResponse.from(consultationSessionRecordRepository.findEntity(id, getCurrentOrganisation()));
    }

    @GetMapping("/api/consultationSessionRecord/{id}/files")
    public List<ConsultationSessionRecordFileContract> getConsultationSessionRecordFiles(@PathVariable("id") int id) {
        ConsultationSessionRecord consultationSessionRecord = consultationSessionRecordRepository.findEntity(id, getCurrentOrganisation());
        return ConsultationSessionRecordFileContract.from(consultationSessionRecord.getFiles());
    }

    @RequestMapping(value = "/api/consultationSessionRecords", method = {RequestMethod.PUT})
    public void save(@RequestBody List<ConsultationSessionRecordRequest> requests) throws IOException {
        for (ConsultationSessionRecordRequest request : requests) {
            save(request);
        }
    }

    @Transactional
    @RequestMapping(value = "/api/consultationSessionRecord", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity save(@RequestBody ConsultationSessionRecordRequest request) throws IOException {
        ConsultationSessionRecord entity = Repository.findByIdOrCreate(request.getId(), getCurrentOrganisation(), consultationSessionRecordRepository, new ConsultationSessionRecord());
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
            client.addConsultationSessionRecord(entity);
            clientRepository.save(client);
        } else {
            consultationSessionRecordRepository.save(entity);
        }
        return new ResponseEntity<>(entity.getId(), HttpStatus.OK);
    }
}
