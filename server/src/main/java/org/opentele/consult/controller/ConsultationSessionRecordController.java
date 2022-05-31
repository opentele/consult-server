package org.opentele.consult.controller;

import org.opentele.consult.config.ApplicationConfig;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    @RequestMapping(value = "/api/consultationSessionRecords", method = {RequestMethod.PUT})
    public void save(@RequestBody List<ConsultationSessionRecordRequest> requests) {
        requests.forEach(this::save);
    }

    @Transactional
    @RequestMapping(value = "/api/consultationSessionRecord", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity save(ConsultationSessionRecordRequest request) {
        try {
            ConsultationSessionRecord entity = Repository.findByIdOrCreate(request.getId(), getCurrentOrganisation(), consultationSessionRecordRepository, new ConsultationSessionRecord());
            entity.setComplaints(request.getComplaints());
            entity.setObservations(request.getObservations());
            entity.setRecommendations(request.getRecommendations());
            entity.setKeyInference(request.getKeyInference());
            for (MultipartFile multipartFile : request.getFiles()) {
                if (fileUtil.getSizeInMB(multipartFile) > applicationConfig.getMaxFileSizeInMegabytes()) {
                    rollback();
                    return new ResponseEntity<>(String.format("File larger than allowed size. Max size allowed is: %s MB", applicationConfig.getMaxFileSizeInMegabytes()), HttpStatus.BAD_REQUEST);
                }
            }

            for (MultipartFile multipartFile : request.getFiles()) {
                String fileName = fileUtil.saveFile(applicationConfig.getAttachmentsLocation(), multipartFile);
                ConsultationSessionRecordFile file = new ConsultationSessionRecordFile();
                file.setFileName(fileName);
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
        } catch (IOException e) {
            rollback();
            return new ResponseEntity<>(String.format("Could not save file due to: %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
