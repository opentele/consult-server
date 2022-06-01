package org.opentele.consult.controller;

import org.opentele.consult.config.ApplicationConfig;
import org.opentele.consult.domain.client.ConsultationSessionRecord;
import org.opentele.consult.domain.framework.ConsultationSessionRecordFile;
import org.opentele.consult.framework.FileUtil;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.repository.ConsultationSessionRecordRepository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@PreAuthorize("hasRole('User')")
public class ConsultationSessionRecordFileController extends BaseController {
    private final FileUtil fileUtil;
    private final ApplicationConfig applicationConfig;
    private final ConsultationSessionRecordRepository consultationSessionRecordRepository;

    @Autowired
    public ConsultationSessionRecordFileController(UserService userService, UserSession userSession, FileUtil fileUtil, ApplicationConfig applicationConfig, ConsultationSessionRecordRepository consultationSessionRecordRepository) {
        super(userService, userSession);
        this.fileUtil = fileUtil;
        this.applicationConfig = applicationConfig;
        this.consultationSessionRecordRepository = consultationSessionRecordRepository;
    }

    @RequestMapping(value = "/api/consultationSessionRecordFile", method = {RequestMethod.POST})
    public ResponseEntity uploadFile(@RequestParam(name = "file") MultipartFile multipartFile) {
        if (fileUtil.getSizeInMB(multipartFile) > applicationConfig.getMaxFileSizeInMegabytes()) {
            return new ResponseEntity<>(String.format("File larger than allowed size. Max size allowed is: %s MB", applicationConfig.getMaxFileSizeInMegabytes()), HttpStatus.BAD_REQUEST);
        }

        try {
            String fileName = fileUtil.saveFile(applicationConfig.getAttachmentsLocation(), multipartFile);
            return new ResponseEntity<>(fileName, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(String.format("Could not save file due to: %s", e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/api/consultationSessionRecordFile/{id}")
    @Transactional
    public void deleteFile(@PathVariable("id") int id) {
        ConsultationSessionRecord consultationSessionRecord = consultationSessionRecordRepository.findByFilesId(id);
        ConsultationSessionRecordFile file = consultationSessionRecord.removeFile(id);
        fileUtil.delete(applicationConfig.getAttachmentsLocation(), file.getFileName());
    }
}
