package org.opentele.consult.contract.client;

import org.opentele.consult.domain.client.Client;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ClientContract extends BaseClientContract {
    private LocalDate dateOfBirth;
    private List<ConsultationSessionRecordContract> consultationSessionRecords;

    public static ClientContract from(Client client) {
        ClientContract contract = new ClientContract();
        contract.setId(client.getId());
        contract.setGender(client.getGender());
        contract.setName(client.getName());
        contract.setRegistrationNumber(client.getRegistrationNumber());
        contract.setAge(client.getAge());
        contract.setConsultationSessionRecords(client.getConsultationSessionRecords().stream().map(ConsultationSessionRecordResponse::from).collect(Collectors.toList()));
        return contract;
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
