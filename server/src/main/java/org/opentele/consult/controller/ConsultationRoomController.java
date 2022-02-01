package org.opentele.consult.controller;

import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/consultationRoom")
public class ConsultationRoomController {
    public List<ConsultationRoom> getActiveRooms() {
        return new ArrayList<>();
    }
}
