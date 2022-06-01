package org.opentele.consult.contract.client;

import org.opentele.consult.contract.framework.BaseEntityContract;
import org.opentele.consult.domain.framework.ConsultationSessionRecordFile;

import java.util.List;
import java.util.stream.Collectors;

public class ConsultationSessionRecordFileContract extends BaseEntityContract {
    private String name;
    private String file;

    public static List<ConsultationSessionRecordFileContract> from(List<ConsultationSessionRecordFile> files) {
        return files.stream().map(ConsultationSessionRecordFileContract::new).collect(Collectors.toList());
    }

    public ConsultationSessionRecordFileContract() {
    }

    public ConsultationSessionRecordFileContract(ConsultationSessionRecordFile consultationSessionRecordFile) {
        super(consultationSessionRecordFile);
        this.name = consultationSessionRecordFile.getName();
        this.file = consultationSessionRecordFile.getFileName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}
