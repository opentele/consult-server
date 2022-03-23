package org.opentele.consult.controller;

import org.opentele.consult.contract.client.ClientSearchResponse;
import org.opentele.consult.contract.client.ConsultationRoomClientResponse;
import org.opentele.consult.contract.consultationRoom.BaseConsultationRoomContract;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomResponse;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleRequest;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleResponse;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomScheduleUser;
import org.opentele.consult.domain.consultationRoom.ConsultationRooms;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.mapper.consultationRoom.ConsultationRoomMapper;
import org.opentele.consult.mapper.consultationRoom.ConsultationRoomScheduleMapper;
import org.opentele.consult.repository.ClientRepository;
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
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ConsultationRoomController extends BaseController {
    private final ConsultationRoomRepository consultationRoomRepository;
    private final ConsultationRoomScheduleRepository consultationRoomScheduleRepository;
    private final ConsultationRoomMapper consultationRoomMapper;
    private final ClientRepository clientRepository;

    @Autowired
    public ConsultationRoomController(ConsultationRoomRepository consultationRoomRepository,
                                      ConsultationRoomScheduleRepository consultationRoomScheduleRepository,
                                      UserService userService, ConsultationRoomMapper consultationRoomMapper,
                                      UserSession userSession, ClientRepository clientRepository) {
        super(userService, userSession);
        this.consultationRoomRepository = consultationRoomRepository;
        this.consultationRoomScheduleRepository = consultationRoomScheduleRepository;
        this.consultationRoomMapper = consultationRoomMapper;
        this.clientRepository = clientRepository;
    }

    @GetMapping(value = "/api/consultationRoom/today")
    @PreAuthorize("hasRole('User')")
    public List<ConsultationRoomResponse> getTodayRooms(Principal principal) {
        LocalDate today = LocalDate.now();
        return getConsultationRooms(today, today, principal);
    }

    @GetMapping(value = "/api/consultationRoom/past")
    @PreAuthorize("hasRole('User')")
    public List<ConsultationRoomResponse> getPastRooms(Principal principal) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate twoWeeksBack = yesterday.minusWeeks(2);
        return getConsultationRooms(twoWeeksBack, yesterday, principal);
    }

    @GetMapping(value = "/api/consultationRoom/future")
    @PreAuthorize("hasRole('User')")
    public List<ConsultationRoomResponse> getFutureRooms(Principal principal) {
        LocalDate today = LocalDate.now();
        LocalDate oneMonthLater = today.plusMonths(1);
        return getConsultationRooms(today, oneMonthLater, principal);
    }

    private List<ConsultationRoomResponse> getConsultationRooms(LocalDate startDate, LocalDate endDate, Principal principal) {
        Map<LocalDate, List<ConsultationRoomResponse>> map = new HashMap<>();
        ConsultationRooms consultationRooms = consultationRoomRepository.findAllBetween(startDate, endDate, getCurrentOrganisation());
        return consultationRooms.stream().map(consultationRoom -> consultationRoomMapper.map(consultationRoom, getCurrentUser(principal))).collect(Collectors.toList());
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

    @GetMapping(value = "/api/consultationRoom/client")
    public List<ConsultationRoomClientResponse> getClients(@RequestParam(name = "consultationRoomId") int consultationRoomId) {
        return clientRepository.getClients(consultationRoomId).stream().map(projection -> {
            ConsultationRoomClientResponse response = new ConsultationRoomClientResponse();
            response.setGender(projection.getGender());
            response.setQueueNumber(projection.getQueueNumber());
            response.setAge(Period.between(LocalDate.now(), projection.getDateOfBirth()));
            response.setName(projection.getName());
            response.setRegistrationNumber(projection.getRegistrationNumber());
            return response;
        }).collect(Collectors.toList());
    }

    @GetMapping("/api/consultationRoom/client/search")
    public List<ClientSearchResponse> searchResults(@RequestParam("q") String q,
                                                    @RequestParam("consultationRoomId") int consultationRoomId) {
        return clientRepository.getClientsMatching(q, getCurrentOrganisation(), consultationRoomId).stream().map(ClientSearchResponse::from).collect(Collectors.toList());
    }
}
