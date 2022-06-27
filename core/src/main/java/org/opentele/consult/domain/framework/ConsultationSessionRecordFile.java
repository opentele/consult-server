package org.opentele.consult.domain.framework;

import org.opentele.consult.domain.client.ConsultationSessionRecord;

import javax.persistence.*;

@Entity
public class ConsultationSessionRecordFile extends OrganisationalEntity {
    @Column
    private String name;

    @Column
    private String fileName;

    @Column
    private String mimeType;

    @ManyToOne(targetEntity = ConsultationSessionRecord.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_session_record_id", columnDefinition = "integer not null")
    private ConsultationSessionRecord consultationSessionRecord;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ConsultationSessionRecord getConsultationSessionRecord() {
        return consultationSessionRecord;
    }

    public void setConsultationSessionRecord(ConsultationSessionRecord consultationSessionRecord) {
        this.consultationSessionRecord = consultationSessionRecord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
