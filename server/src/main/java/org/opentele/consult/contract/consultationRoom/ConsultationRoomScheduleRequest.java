package org.opentele.consult.contract.consultationRoom;

import java.time.LocalTime;
import java.util.List;

public class ConsultationRoomScheduleRequest {
    private String title;
    private int totalSlots;
    private List<Integer> providers;
    private String recurrenceRule;
    private LocalTime startTime;
    private LocalTime endTime;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public List<Integer> getProviders() {
        return providers;
    }

    public void setProviders(List<Integer> providers) {
        this.providers = providers;
    }

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
