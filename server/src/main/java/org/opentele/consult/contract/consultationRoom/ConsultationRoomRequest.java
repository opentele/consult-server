package org.opentele.consult.contract.consultationRoom;

import org.opentele.consult.contract.framework.BaseEntityContract;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultationRoomRequest extends BaseEntityContract {
    private String title;
    private int totalSlots;
    private List<Integer> providers = new ArrayList<>();
    private LocalTime scheduledStartTime;
    private LocalTime scheduledEndTime;
    private LocalDate scheduledOn;

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

    public LocalDate getScheduledOn() {
        return scheduledOn;
    }

    public void setScheduledOn(LocalDate scheduledOn) {
        this.scheduledOn = scheduledOn;
    }
}
