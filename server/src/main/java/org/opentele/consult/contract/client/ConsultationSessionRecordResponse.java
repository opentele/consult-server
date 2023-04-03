package org.opentele.consult.contract.client;

import org.opentele.consult.domain.client.ConsultationRecord;
import org.opentele.consult.util.DateTimeUtil;

import java.time.LocalDateTime;

public class ConsultationSessionRecordResponse extends ConsultationSessionRecordContract {
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private String createdBy;
    private String lastModifiedBy;

    public static ConsultationSessionRecordResponse from(ConsultationRecord entity) {
        ConsultationSessionRecordResponse contract = new ConsultationSessionRecordResponse();
        contract.setId(entity.getId());
        contract.setComplaints(entity.getComplaints());
        contract.setObservations(entity.getObservations());
        contract.setRecommendations(entity.getRecommendations());
        contract.setKeyInference(entity.getKeyInference());
        contract.setCreatedOn(DateTimeUtil.fromDate(entity.getCreatedDate()));
        contract.setUpdatedOn(DateTimeUtil.fromDate(entity.getLastModifiedDate()));
        contract.setClientId(entity.getClient().getId());
        if (entity.getConsultationRoom() != null)
            contract.setConsultationRoomId(entity.getConsultationRoom().getId());
        contract.setCreatedBy(entity.getCreatedBy().getDetailsForClient());
        contract.setLastModifiedBy(entity.getLastModifiedBy().getDetailsForClient());
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
}
