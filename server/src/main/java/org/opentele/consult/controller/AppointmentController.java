package org.opentele.consult.controller;

import org.opentele.consult.repository.ConsultationRoomRepository;
import org.opentele.consult.repository.ConsultationRoomScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppointmentController {
    private final ConsultationRoomScheduleRepository consultationRoomScheduleRepository;
    private final ConsultationRoomRepository consultationRoomRepository;

    @Autowired
    public AppointmentController(ConsultationRoomScheduleRepository consultationRoomScheduleRepository, ConsultationRoomRepository consultationRoomRepository) {
        this.consultationRoomScheduleRepository = consultationRoomScheduleRepository;
        this.consultationRoomRepository = consultationRoomRepository;
    }

    @PostMapping("/api/appointment")
    public void addAppointment() {

    }
}
