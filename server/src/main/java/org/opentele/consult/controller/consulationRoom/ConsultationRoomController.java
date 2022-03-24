package org.opentele.consult.controller.consulationRoom;

import org.opentele.consult.contract.client.ClientSearchResponse;
import org.opentele.consult.contract.client.ConsultationRoomClientResponse;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleContract;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomResponse;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleRequest;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleResponse;
import org.opentele.consult.controller.BaseController;
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
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate oneMonthLater = tomorrow.plusMonths(1);
        return getConsultationRooms(tomorrow, oneMonthLater, principal);
    }

    private List<ConsultationRoomResponse> getConsultationRooms(LocalDate startDate, LocalDate endDate, Principal principal) {
        Map<LocalDate, List<ConsultationRoomResponse>> map = new HashMap<>();
        ConsultationRooms consultationRooms = consultationRoomRepository.findAllBetween(startDate, endDate, getCurrentOrganisation());
        return consultationRooms.stream().map(consultationRoom -> consultationRoomMapper.map(consultationRoom, getCurrentUser(principal))).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('User')")
    @RequestMapping(value = "/api/consultationRoom", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<Integer> save(@RequestBody ConsultationRoomScheduleContract request, Principal principal) {
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
