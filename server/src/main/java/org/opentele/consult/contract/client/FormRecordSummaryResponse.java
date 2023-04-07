package org.opentele.consult.contract.client;

import org.opentele.consult.domain.client.ConsultationFormRecord;
import org.opentele.consult.util.DateTimeUtil;

import java.time.LocalDate;

public class FormRecordSummaryResponse {
    private final String formId;
    private final long formRecordId;
    private final LocalDate creationDate;

    public FormRecordSummaryResponse(ConsultationFormRecord consultationFormRecord) {
        this.formRecordId = consultationFormRecord.getId();
        this.creationDate = DateTimeUtil.fromDateToLocal(consultationFormRecord.getCreatedDate());
        this.formId = consultationFormRecord.getFormId();
    }

    public long getFormRecordId() {
        return formRecordId;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public String getFormId() {
        return formId;
    }
}
