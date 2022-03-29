package org.opentele.consult.controller.consulationRoom;

import org.opentele.consult.contract.client.ClientSearchResponse;
import org.opentele.consult.contract.client.ClientSearchResults;
import org.opentele.consult.contract.client.ConsultationRoomClientResponse;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomConferenceResponse;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomContract;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleContract;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomDetailResponse;
import org.opentele.consult.controller.BaseController;
import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.opentele.consult.domain.consultationRoom.ConsultationRooms;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.mapper.consultationRoom.ConsultationRoomMapper;
import org.opentele.consult.repository.ClientRepository;
import org.opentele.consult.repository.ConsultationRoomRepository;
import org.opentele.consult.service.ConsultationRoomService;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('User')")
public class ConsultationRoomController extends BaseController {
    private final ConsultationRoomRepository consultationRoomRepository;
    private final ConsultationRoomMapper consultationRoomMapper;
    private final ClientRepository clientRepository;
    private final ConsultationRoomService consultationRoomService;

    @Autowired
    public ConsultationRoomController(ConsultationRoomRepository consultationRoomRepository,
                                      UserService userService, ConsultationRoomMapper consultationRoomMapper,
                                      UserSession userSession, ClientRepository clientRepository, ConsultationRoomService consultationRoomService) {
        super(userService, userSession);
        this.consultationRoomRepository = consultationRoomRepository;
        this.consultationRoomMapper = consultationRoomMapper;
        this.clientRepository = clientRepository;
        this.consultationRoomService = consultationRoomService;
    }

    @GetMapping(value = "/api/consultationRoom/today")
    public List<ConsultationRoomDetailResponse> getTodayRooms(Principal principal) {
        LocalDate today = LocalDate.now();
        return getConsultationRooms(today, today, principal);
    }

    @GetMapping(value = "/api/consultationRoom/past")
    public List<ConsultationRoomDetailResponse> getPastRooms(Principal principal) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate twoWeeksBack = yesterday.minusWeeks(2);
        return getConsultationRooms(twoWeeksBack, yesterday, principal);
    }

    @GetMapping(value = "/api/consultationRoom/future")
    public List<ConsultationRoomDetailResponse> getFutureRooms(Principal principal) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate oneMonthLater = tomorrow.plusMonths(1);
        return getConsultationRooms(tomorrow, oneMonthLater, principal);
    }

    private List<ConsultationRoomDetailResponse> getConsultationRooms(LocalDate startDate, LocalDate endDate, Principal principal) {
        Map<LocalDate, List<ConsultationRoomDetailResponse>> map = new HashMap<>();
        ConsultationRooms consultationRooms = consultationRoomRepository.findAllBetween(startDate, endDate, getCurrentOrganisation());
        return consultationRooms.stream().map(consultationRoom -> consultationRoomMapper.map(consultationRoom, getCurrentUser(principal))).collect(Collectors.toList());
    }

    @GetMapping(value = "/api/consultationRoom/{id}")
    public ConsultationRoomContract get(@PathVariable("id") int id) {
        return consultationRoomMapper.map(consultationRoomRepository.findEntity(id));
    }

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
    public ClientSearchResults searchResults(@RequestParam("q") String q,
                                             @RequestParam("consultationRoomId") int consultationRoomId) {
        int count = clientRepository.countClientsMatching(q, getCurrentOrganisation(), consultationRoomId);
        List<ClientSearchResponse> clientSearchResponses = clientRepository.getClientsMatching(q, getCurrentOrganisation(), consultationRoomId).stream().map(ClientSearchResponse::from).collect(Collectors.toList());
        return new ClientSearchResults(count, clientSearchResponses);
    }

    @RequestMapping(value = "/api/consultationRoom/teleConference", method = {RequestMethod.POST, RequestMethod.PUT})
    public int saveTeleConference(@RequestBody int consultationRoomId) {
        consultationRoomService.setup(consultationRoomId);
        return consultationRoomId;
    }

    @GetMapping("/api/consultationRoom/teleConference/{consultationRoomId}")
    public ConsultationRoomConferenceResponse getConsultationRoomTeleConference(@PathVariable("consultationRoomId") int id, Principal principal) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(id);
        return consultationRoomMapper.mapForConference(consultationRoom, getCurrentUser(principal));
    }

    @PostMapping("/api/consultationRoom/appointmentToken/next")
    public void moveToNextToken(@RequestParam("consultationRoomId") int consultationRoomId, Principal principal) {
        consultationRoomService.moveToNextToken(consultationRoomId);
    }

    @PostMapping("/api/consultationRoom/appointmentToken/previous")
    public void moveToPreviousToken(@RequestParam("consultationRoomId") int consultationRoomId, Principal principal) {
        consultationRoomService.moveToPreviousToken(consultationRoomId);
    }
}
