package org.opentele.consult.controller;

import org.opentele.consult.contract.consultationRoom.ConsultationRoomResponse;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleResponse;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.repository.ConsultationRoomRepository;
import org.opentele.consult.repository.ConsultationRoomScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@RestController
public class ConsultationRoomController {
    private final ConsultationRoomRepository consultationRoomRepository;
    private final ConsultationRoomScheduleRepository consultationRoomScheduleRepository;

    @Autowired
    public ConsultationRoomController(ConsultationRoomRepository consultationRoomRepository, ConsultationRoomScheduleRepository consultationRoomScheduleRepository) {
        this.consultationRoomRepository = consultationRoomRepository;
        this.consultationRoomScheduleRepository = consultationRoomScheduleRepository;
    }

    @GetMapping(value = "/api/consultationRoom/active")
    public List<ConsultationRoomResponse> getActiveRooms() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime tomorrow = LocalDate.now().plus(1, DAYS).atStartOfDay();
        List<ConsultationRoom> consultationRooms = consultationRoomRepository.findAllByScheduledStartAtAfterAndScheduledEndAtBefore(today, tomorrow);
        return new ArrayList<>();
    }

    @GetMapping(value = "/api/consultationRoom/between")
    public List<ConsultationRoomResponse> getConsultationsBetween(@RequestParam("startDate") LocalDate startDate,
                                                                  @RequestParam("endDate") LocalDate endDate) {
        List<ConsultationRoom> consultationRooms = consultationRoomRepository.findAllByScheduledStartAtAfterAndScheduledEndAtBefore(startDate.atStartOfDay(), endDate.atStartOfDay());
        return new ArrayList<>();
    }

    @GetMapping(value = "/api/consultationRoomSchedule")
    public List<ConsultationRoomScheduleResponse> getScheduledConsultations() {
        Iterable<ConsultationRoomSchedule> consultationRoomSchedules = consultationRoomScheduleRepository.findAll();
        return new ArrayList<>();
    }
}
