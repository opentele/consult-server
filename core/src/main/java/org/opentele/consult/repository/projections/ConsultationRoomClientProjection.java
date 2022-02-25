package org.opentele.consult.repository.projections;

import org.opentele.consult.domain.client.Gender;

import java.time.LocalDate;

public interface ConsultationRoomClientProjection {
    public String getName();
    public String getRegistrationNumber();
    public int getQueueNumber();
    public Gender getGender();
    public LocalDate getDateOfBirth();
}
