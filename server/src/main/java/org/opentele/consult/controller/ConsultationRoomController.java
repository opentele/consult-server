package org.opentele.consult.controller;

import org.opentele.consult.contract.consultationRoom.ConsultationRoomResponse;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleRequest;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleResponse;
import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomScheduleUser;
import org.opentele.consult.domain.consultationRoom.ConsultationRooms;
import org.opentele.consult.mapper.consultationRoom.ConsultationRoomMapper;
import org.opentele.consult.mapper.consultationRoom.ConsultationRoomScheduleMapper;
import org.opentele.consult.repository.ConsultationRoomRepository;
import org.opentele.consult.repository.ConsultationRoomScheduleRepository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ConsultationRoomController {
    private final ConsultationRoomRepository consultationRoomRepository;
    private final ConsultationRoomScheduleRepository consultationRoomScheduleRepository;
    private final ConsultationRoomMapper consultationRoomMapper;
    private final UserService userService;

    @Autowired
    public ConsultationRoomController(ConsultationRoomRepository consultationRoomRepository,
                                      ConsultationRoomScheduleRepository consultationRoomScheduleRepository,
                                      UserService userService, ConsultationRoomMapper consultationRoomMapper) {
        this.consultationRoomRepository = consultationRoomRepository;
        this.consultationRoomScheduleRepository = consultationRoomScheduleRepository;
        this.consultationRoomMapper = consultationRoomMapper;
        this.userService = userService;
    }

    @GetMapping(value = "/api/consultationRoom/today")
    @PreAuthorize("hasRole('User')")
    public List<ConsultationRoomResponse> getActiveRooms(Principal principal) {
        LocalDate today = LocalDate.now();
        List<ConsultationRoomResponse> consultationRoomResponses = getConsultations(today, today, principal).get(today);
        if (consultationRoomResponses == null) return new ArrayList<>();
        return consultationRoomResponses;
    }

    @GetMapping(value = "/api/consultationRoom/between")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity getConsultationsBetween
            (@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
             @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
             Principal principal) {
        Map<LocalDate, List<ConsultationRoomResponse>> map = getConsultations(startDate, endDate, principal);
        return new ResponseEntity(map, HttpStatus.OK);
    }

    private Map<LocalDate, List<ConsultationRoomResponse>> getConsultations(LocalDate startDate, LocalDate endDate, Principal principal) {
        Map<LocalDate, List<ConsultationRoomResponse>> map = new HashMap<>();
        ConsultationRooms consultationRooms = consultationRoomRepository.findAllBetween(startDate, endDate, userService.getOrganisation(principal));
        consultationRooms.forEach(consultationRoom -> {
            LocalDate date = consultationRoom.getScheduledOn();
            map.computeIfAbsent(date, k -> new ArrayList<>());
            map.get(date).add(consultationRoomMapper.map(consultationRoom, userService.getUser(principal)));
        });
        return map;
    }

    @GetMapping(value = "/api/consultationRoomSchedule")
    @PreAuthorize("hasRole('User')")
    public List<ConsultationRoomScheduleResponse> getScheduledConsultations(Principal principal) {
        Organisation organisation = userService.getOrganisation(principal);
        List<ConsultationRoomSchedule> list = consultationRoomScheduleRepository.findAllByOrganisation(organisation);
        return list.stream().map(ConsultationRoomScheduleMapper::map).collect(Collectors.toList());
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
