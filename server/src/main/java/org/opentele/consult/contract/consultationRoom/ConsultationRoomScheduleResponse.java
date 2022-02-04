package org.opentele.consult.contract.consultationRoom;

import java.time.LocalTime;

public class ConsultationRoomScheduleResponse extends BaseConsultationRoomResponse {
    private String recurrenceRule;
    private LocalTime startTime;
    private LocalTime endTime;

    public String getRecurrenceRule() {
        return recurrenceRule;
    }

    public void setRecurrenceRule(String recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}