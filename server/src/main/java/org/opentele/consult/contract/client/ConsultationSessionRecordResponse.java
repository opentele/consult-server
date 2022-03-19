package org.opentele.consult.contract.client;

import org.opentele.consult.domain.client.ConsultationSessionRecord;
import org.opentele.consult.util.DateTimeUtil;

import java.time.LocalDateTime;

public class ConsultationSessionRecordResponse extends ConsultationSessionRecordContract {
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;

    public static ConsultationSessionRecordResponse from(ConsultationSessionRecord entity) {
        ConsultationSessionRecordResponse contract = new ConsultationSessionRecordResponse();
        contract.setId(entity.getId());
        contract.setComplaints(entity.getComplaints());
        contract.setObservations(entity.getObservations());
        contract.setRecommendations(entity.getRecommendations());
        contract.setKeyInference(entity.getKeyInference());
        contract.setCreatedOn(DateTimeUtil.fromDate(entity.getCreatedDate()));
        contract.setUpdatedOn(DateTimeUtil.fromDate(entity.getLastModifiedDate()));
        contract.setClientId(entity.getClient().getId());
        return contract;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }
}
