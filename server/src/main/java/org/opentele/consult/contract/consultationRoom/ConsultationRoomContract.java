package org.opentele.consult.contract.consultationRoom;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultationRoomContract extends ConsultationRoomScheduleContract {
    private LocalDate scheduledOn;
    private LocalTime scheduledStartTime;
    private LocalTime scheduledEndTime;
    private int scheduleId;

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

}
