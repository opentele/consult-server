package org.opentele.consult.contract.client;

import org.opentele.consult.contract.framework.BaseEntityContract;
import org.opentele.consult.domain.framework.ConsultationSessionRecordFile;

import java.util.List;
import java.util.stream.Collectors;

public class ConsultationSessionRecordFileContract extends BaseEntityContract {
    private String mimeType;
    private String name;
    private String fileName;

    public static List<ConsultationSessionRecordFileContract> from(List<ConsultationSessionRecordFile> files) {
        return files.stream().map(ConsultationSessionRecordFileContract::new).collect(Collectors.toList());
    }

    public ConsultationSessionRecordFileContract() {
    }

    public ConsultationSessionRecordFileContract(ConsultationSessionRecordFile consultationSessionRecordFile) {
        super(consultationSessionRecordFile);
        this.name = consultationSessionRecordFile.getName();
        this.fileName = consultationSessionRecordFile.getFileName();
        this.mimeType = consultationSessionRecordFile.getMimeType();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
