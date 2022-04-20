package org.opentele.consult.contract.client;

import org.opentele.consult.domain.client.Client;
import org.opentele.consult.util.DateTimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClientContract extends BaseClientContract {
    private String createdBy;
    private String lastModifiedBy;
    private LocalDateTime createdOn;
    private LocalDateTime updatedOn;
    private LocalDate dateOfBirth;
    private List<ConsultationSessionRecordContract> consultationSessionRecords = new ArrayList<>();

    public static ClientContract fromWithChildren(Client client) {
        ClientContract contract = from(client);
        mapChildren(client, contract);
        return contract;
    }

    public static ClientContract from(Client client) {
        ClientContract contract = new ClientContract();
        contract.setId(client.getId());
        contract.setGender(client.getGender());
        contract.setName(client.getName());
        contract.setRegistrationNumber(client.getRegistrationNumber());
        contract.setAge(client.getAge());
        contract.setCreatedBy(client.getCreatedBy().getName());
        contract.setLastModifiedBy(client.getLastModifiedBy().getName());
        contract.setCreatedOn(DateTimeUtil.fromDate(client.getCreatedDate()));
        contract.setUpdatedOn(DateTimeUtil.fromDate(client.getLastModifiedDate()));
        return contract;
    }

    public static void mapChildren(Client client, ClientContract contract) {
        contract.setConsultationSessionRecords(client.getConsultationSessionRecords().stream().map(ConsultationSessionRecordResponse::from).collect(Collectors.toList()));
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public List<ConsultationSessionRecordContract> getConsultationSessionRecords() {
        return consultationSessionRecords;
    }

    public void setConsultationSessionRecords(List<ConsultationSessionRecordContract> consultationSessionRecords) {
        this.consultationSessionRecords = consultationSessionRecords;
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
