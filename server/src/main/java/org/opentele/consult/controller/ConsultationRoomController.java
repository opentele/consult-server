package org.opentele.consult.controller;

import org.opentele.consult.contract.consultationRoom.ConsultationRoomResponse;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleRequest;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleResponse;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomScheduleUser;
import org.opentele.consult.mapper.ConsultationRoomScheduleMapper;
import org.opentele.consult.repository.ConsultationRoomRepository;
import org.opentele.consult.repository.ConsultationRoomScheduleRepository;
import org.opentele.consult.repository.framework.Repository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

@RestController
public class ConsultationRoomController {
    private final ConsultationRoomRepository consultationRoomRepository;
    private final ConsultationRoomScheduleRepository consultationRoomScheduleRepository;
    private final UserService userService;

    @Autowired
    public ConsultationRoomController(ConsultationRoomRepository consultationRoomRepository,
                                      ConsultationRoomScheduleRepository consultationRoomScheduleRepository,
                                      UserService userService) {
        this.consultationRoomRepository = consultationRoomRepository;
        this.consultationRoomScheduleRepository = consultationRoomScheduleRepository;
        this.userService = userService;
    }

    @GetMapping(value = "/api/consultationRoom/active")
    @PreAuthorize("hasRole('User')")
    public List<ConsultationRoomResponse> getActiveRooms() {
        LocalDateTime today = LocalDate.now().atStartOfDay();
        LocalDateTime tomorrow = LocalDate.now().plus(1, DAYS).atStartOfDay();
        List<ConsultationRoom> consultationRooms = consultationRoomRepository.findAllByScheduledStartAtAfterAndScheduledEndAtBefore(today, tomorrow);
        return new ArrayList<>();
    }

    @GetMapping(value = "/api/consultationRoom/between")
    @PreAuthorize("hasRole('User')")
    public List<ConsultationRoomResponse> getConsultationsBetween(@RequestParam("startDate") LocalDate startDate,
                                                                  @RequestParam("endDate") LocalDate endDate) {
        List<ConsultationRoom> consultationRooms = consultationRoomRepository.findAllByScheduledStartAtAfterAndScheduledEndAtBefore(startDate.atStartOfDay(), endDate.atStartOfDay());
        return new ArrayList<>();
    }

    @GetMapping(value = "/api/consultationRoomSchedule")
    @PreAuthorize("hasRole('User')")
    public List<ConsultationRoomScheduleResponse> getScheduledConsultations() {
        Iterable<ConsultationRoomSchedule> consultationRoomSchedules = consultationRoomScheduleRepository.findAll();
        return new ArrayList<>();
    }

    @PutMapping(value = "/api/consultationRoomSchedule")
    @PreAuthorize("hasRole('OrgAdmin')")
    public ResponseEntity<Integer> put(@RequestBody ConsultationRoomScheduleRequest request, Principal principal) {
        ConsultationRoomSchedule consultationRoomSchedule = ConsultationRoomScheduleMapper.mapNew(request, userService.getUser(principal));
        Set<ConsultationRoomScheduleUser> consultationRoomScheduleUsers = request.getProviders().stream().map(userId -> {
            ConsultationRoomScheduleUser consultationRoomScheduleUser = new ConsultationRoomScheduleUser();
            consultationRoomScheduleUser.setConsultationRoomSchedule(consultationRoomSchedule);
            consultationRoomScheduleUser.setUser(userService.getUser(userId));
            return consultationRoomScheduleUser;
        }).collect(Collectors.toSet());
        consultationRoomSchedule.setProviders(consultationRoomScheduleUsers);
        ConsultationRoomSchedule saved = consultationRoomScheduleRepository.save(consultationRoomSchedule);
        return new ResponseEntity<>(saved.getId(), HttpStatus.OK);
    }
}
