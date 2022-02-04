package org.opentele.consult.contract.consultationRoom;

import org.opentele.consult.contract.framework.BaseResponse;
import org.opentele.consult.contract.security.ProviderResponse;

import java.util.List;

public class BaseConsultationRoomResponse extends BaseResponse {
    private String title;
    private List<ProviderResponse> providers;
    private int totalSlots;

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
}
