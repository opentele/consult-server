package org.opentele.consult.contract.consultationRoom;

import org.opentele.consult.contract.framework.BaseResponse;
import org.opentele.consult.contract.security.ProviderResponse;

import java.util.List;

public class BaseConsultationRoomResponse extends BaseResponse {
    private String title;
    private List<ProviderResponse> providers;
    private int totalSlots;
}
