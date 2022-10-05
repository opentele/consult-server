package org.opentele.consult.repository.projections;

import org.opentele.consult.domain.client.Gender;

import java.time.LocalDate;

public interface ConsultationRoomClientProjection {
    String getName();
    String getRegistrationNumber();
    int getQueueNumber();
    int getId();
    Gender getGender();
    LocalDate getDateOfBirth();
}
