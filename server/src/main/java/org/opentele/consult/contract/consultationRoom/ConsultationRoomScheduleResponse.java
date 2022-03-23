package org.opentele.consult.contract.consultationRoom;

import java.time.LocalDate;

public class ConsultationRoomScheduleResponse extends ConsultationRoomScheduleContract {
    private String recurrenceRule;
    private LocalDate startDate;

    public String getRecurrenceRule() {
        return recurrenceRule;
    }

    public void setRecurrenceRule(String recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
}
