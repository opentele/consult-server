package org.opentele.consult.controller;

import org.opentele.consult.contract.consultationRoom.BaseConsultationRoomContract;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomResponse;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleRequest;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleResponse;
import org.opentele.consult.domain.Organisation;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomScheduleUser;
import org.opentele.consult.domain.consultationRoom.ConsultationRooms;
import org.opentele.consult.framework.UserSession;
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
public class ConsultationRoomController extends BaseController {
    private final ConsultationRoomRepository consultationRoomRepository;
    private final ConsultationRoomScheduleRepository consultationRoomScheduleRepository;
    private final ConsultationRoomMapper consultationRoomMapper;

    @Autowired
    public ConsultationRoomController(ConsultationRoomRepository consultationRoomRepository,
                                      ConsultationRoomScheduleRepository consultationRoomScheduleRepository,
                                      UserService userService, ConsultationRoomMapper consultationRoomMapper,
                                      UserSession userSession) {
        super(userService, userSession);
        this.consultationRoomRepository = consultationRoomRepository;
        this.consultationRoomScheduleRepository = consultationRoomScheduleRepository;
        this.consultationRoomMapper = consultationRoomMapper;
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
        ConsultationRooms consultationRooms = consultationRoomRepository.findAllBetween(startDate, endDate, getCurrentOrganisation());
        consultationRooms.forEach(consultationRoom -> {
            LocalDate date = consultationRoom.getScheduledOn();
            map.computeIfAbsent(date, k -> new ArrayList<>());
            map.get(date).add(consultationRoomMapper.map(consultationRoom, getCurrentUser(principal)));
        });
        return map;
    }

    @GetMapping(value = "/api/consultationRoomSchedule")
    @PreAuthorize("hasRole('User')")
    public List<ConsultationRoomScheduleResponse> getScheduledConsultations() {
        var organisation = getCurrentOrganisation();
        var list = consultationRoomScheduleRepository.findAllByOrganisation(organisation);
        return list.stream().map(ConsultationRoomScheduleMapper::map).collect(Collectors.toList());
    }

    @PutMapping(value = "/api/consultationRoomSchedule")
    @PreAuthorize("hasRole('OrgAdmin')")
    public ResponseEntity<Integer> put(@RequestBody ConsultationRoomScheduleRequest request, Principal principal) {
        var consultationRoomSchedule = ConsultationRoomScheduleMapper.mapNew(request, getCurrentOrganisation());
        var consultationRoomScheduleUsers = request.getProviders().stream().map(userId -> {
            var consultationRoomScheduleUser = new ConsultationRoomScheduleUser();
            consultationRoomScheduleUser.setConsultationRoomSchedule(consultationRoomSchedule);
            consultationRoomScheduleUser.setUser(getUser(userId));
            return consultationRoomScheduleUser;
        }).collect(Collectors.toSet());
        consultationRoomSchedule.setProviders(consultationRoomScheduleUsers);
        var saved = consultationRoomScheduleRepository.save(consultationRoomSchedule);
        return new ResponseEntity<>(saved.getId(), HttpStatus.OK);
    }

    @PutMapping(value = "/api/consultationRoom")
    @PreAuthorize("hasRole('User')")
    public ResponseEntity<Integer> put(@RequestBody BaseConsultationRoomContract request, Principal principal) {
        return null;
    }
}
