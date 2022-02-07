package org.opentele.consult.contract.consultationRoom;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class ConsultationRoomResponse extends BaseConsultationRoomResponse {
    private LocalDateTime scheduledStartAt;
    private LocalDateTime scheduledEndAt;
    private LocalTime startedAt;
    private LocalTime endAt;

    public LocalDateTime getScheduledStartAt() {
        return scheduledStartAt;
    }

    public void setScheduledStartAt(LocalDateTime scheduledStartAt) {
        this.scheduledStartAt = scheduledStartAt;
    }

    public LocalDateTime getScheduledEndAt() {
        return scheduledEndAt;
    }

    public void setScheduledEndAt(LocalDateTime scheduledEndAt) {
        this.scheduledEndAt = scheduledEndAt;
    }

    public LocalTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalTime endAt) {
        this.endAt = endAt;
    }
}
