package org.opentele.consult.contract.consultationRoom;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConsultationRoomResponse extends BaseConsultationRoomResponse {
    private LocalDate scheduledOn;
    private LocalTime scheduledStartTime;
    private LocalTime scheduledEndTime;
    private LocalTime startTime;
    private LocalTime endTime;
    private int scheduleId;

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endAt) {
        this.endTime = endAt;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public LocalDate getScheduledOn() {
        return scheduledOn;
    }

    public void setScheduledOn(LocalDate scheduledOn) {
        this.scheduledOn = scheduledOn;
    }

    public LocalTime getScheduledStartTime() {
        return scheduledStartTime;
    }

    public void setScheduledStartTime(LocalTime scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }

    public LocalTime getScheduledEndTime() {
        return scheduledEndTime;
    }

    public void setScheduledEndTime(LocalTime scheduledEndTime) {
        this.scheduledEndTime = scheduledEndTime;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
}
