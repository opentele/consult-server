package org.opentele.consult.contract.client;

import org.opentele.consult.domain.client.Client;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ClientContract extends BaseClientContract {
    private String createdBy;
    private String lastModifiedBy;
    private LocalDate dateOfBirth;
    private List<ConsultationSessionRecordContract> consultationSessionRecords;

    public static ClientContract fromWithChidren(Client client) {
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
}
