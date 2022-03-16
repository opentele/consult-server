package org.opentele.consult.contract.client;

import java.time.LocalDate;

public class ClientContract extends BaseClientContract {
    private LocalDate dateOfBirth;

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
