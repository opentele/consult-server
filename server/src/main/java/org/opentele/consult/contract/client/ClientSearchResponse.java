package org.opentele.consult.contract.client;

import org.opentele.consult.domain.client.Client;
import org.opentele.consult.domain.client.Gender;

import java.time.Period;

public class ClientSearchResponse extends BaseClientContract {
    private Period age;
    private int numberOfSessions;

    public static ClientSearchResponse from(Client client) {
        ClientSearchResponse clientSearchResponse = new ClientSearchResponse();
        clientSearchResponse.setId(client.getId());
        clientSearchResponse.setRegistrationNumber(client.getRegistrationNumber());
        clientSearchResponse.setName(client.getName());
        clientSearchResponse.setAge(client.getAge());
        clientSearchResponse.setGender(client.getGender());
        clientSearchResponse.setNumberOfSessions(client.getConsultationSessionRecords().size());
        return clientSearchResponse;
    }

    public int getNumberOfSessions() {
        return numberOfSessions;
    }

    public void setNumberOfSessions(int numberOfSessions) {
        this.numberOfSessions = numberOfSessions;
    }

    public Period getAge() {
        return age;
    }

    public void setAge(Period age) {
        this.age = age;
    }
}
