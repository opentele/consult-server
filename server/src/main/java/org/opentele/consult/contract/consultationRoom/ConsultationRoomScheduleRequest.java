package org.opentele.consult.contract.consultationRoom;

import org.opentele.consult.contract.framework.BaseEntityContract;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultationRoomScheduleRequest extends BaseEntityContract {
    private String title;
    private int totalSlots;
    private List<Long> providers = new ArrayList<>();
    private String recurrenceRule;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate startDate;

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

    public List<Long> getProviders() {
        return providers;
    }

    public void setProviders(List<Long> providers) {
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
