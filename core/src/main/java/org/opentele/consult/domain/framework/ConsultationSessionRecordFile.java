package org.opentele.consult.domain.framework;

import org.opentele.consult.domain.client.ConsultationSessionRecord;

import javax.persistence.*;

@Entity
public class ConsultationSessionRecordFile extends OrganisationalEntity {
    @Column(nullable = false)
    private String fileName;

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
}
