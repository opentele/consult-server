package org.opentele.consult.contract.consultationRoom;

import org.opentele.consult.contract.framework.BaseEntityContract;
import org.opentele.consult.contract.security.ProviderResponse;

import java.time.LocalTime;
import java.util.List;

public class BaseConsultationRoomContract extends BaseEntityContract {
    private String title;
    private LocalTime startTime;
    private LocalTime endTime;
    private int totalSlots;
    private List<ProviderResponse> providers;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ProviderResponse> getProviders() {
        return providers;
    }

    public void setProviders(List<ProviderResponse> providers) {
        this.providers = providers;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
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
