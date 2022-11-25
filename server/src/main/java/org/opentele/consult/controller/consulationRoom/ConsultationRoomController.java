package org.opentele.consult.controller.consulationRoom;

import org.opentele.consult.contract.client.ClientSearchResponse;
import org.opentele.consult.contract.client.ClientSearchResults;
import org.opentele.consult.contract.client.ConsultationRoomClientResponse;
import org.opentele.consult.contract.consultationRoom.*;
import org.opentele.consult.controller.BaseController;
import org.opentele.consult.domain.consultationRoom.*;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.mapper.consultationRoom.ConsultationRoomMapper;
import org.opentele.consult.repository.ClientRepository;
import org.opentele.consult.repository.ConsultationRoomRepository;
import org.opentele.consult.repository.ConsultationRoomUserRepository;
import org.opentele.consult.repository.framework.Repository;
import org.opentele.consult.service.ConsultationRoomService;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
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
    private final ConsultationRoomUserRepository consultationRoomUserRepository;

    @Autowired
    public ConsultationRoomController(ConsultationRoomRepository consultationRoomRepository,
                                      UserService userService, ConsultationRoomMapper consultationRoomMapper,
                                      UserSession userSession, ClientRepository clientRepository,
                                      ConsultationRoomService consultationRoomService, ConsultationRoomUserRepository consultationRoomUserRepository) {
        super(userService, userSession);
        this.consultationRoomRepository = consultationRoomRepository;
        this.consultationRoomMapper = consultationRoomMapper;
        this.clientRepository = clientRepository;
        this.consultationRoomService = consultationRoomService;
        this.consultationRoomUserRepository = consultationRoomUserRepository;
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
        return consultationRoomMapper.map(consultationRoomRepository.findEntity(id, getCurrentOrganisation()));
    }

    @RequestMapping(value = "/api/consultationRoom", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<Integer> putPost(@RequestBody ConsultationRoomRequest request, Principal principal) {
        ConsultationRoom consultationRoom = Repository.findByIdOrCreate(request.getId(), getCurrentOrganisation(), consultationRoomRepository, new ConsultationRoom());
        consultationRoom.setOrganisation(getCurrentOrganisation());
        consultationRoom.setTitle(request.getTitle());
        consultationRoom.setTotalSlots(request.getTotalSlots());
        consultationRoom.setScheduledOn(request.getScheduledOn());
        consultationRoom.setScheduledStartTime(request.getScheduledStartTime());
        consultationRoom.setScheduledEndTime(request.getScheduledEndTime());
        List<Integer> existingProviderIds = consultationRoom.getProviders().stream().map(x -> x.getUser().getId()).collect(Collectors.toList());
        Repository.mergeChildren(request.getProviders(), existingProviderIds,
                x -> consultationRoom.removeProvider((ConsultationRoomUser) x),
                x -> consultationRoom.addProvider((ConsultationRoomUser) x),
                x -> consultationRoomUserRepository.findByConsultationRoomAndUserIdAndOrganisation(consultationRoom, x, getCurrentOrganisation()),
                x -> {
                    var consultationRoomUser = new ConsultationRoomUser();
                    consultationRoomUser.setUser(userService.getUser(x));
                    consultationRoomUser.setOrganisation(getCurrentOrganisation());
                    return consultationRoomUser;
                });
        ConsultationRoom saved = consultationRoomRepository.save(consultationRoom);
        return new ResponseEntity<>(saved.getId(), HttpStatus.OK);
    }

    @GetMapping(value = "/api/consultationRoom/client")
    public List<ConsultationRoomClientResponse> getClients(@RequestParam(name = "consultationRoomId") int consultationRoomId) {
        return clientRepository.getClients(consultationRoomId).stream().map(projection -> {
            ConsultationRoomClientResponse response = new ConsultationRoomClientResponse();
            response.setId(projection.getId());
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
        consultationRoomService.setup(consultationRoomId, getCurrentOrganisation());
        return consultationRoomId;
    }

    @GetMapping("/api/consultationRoom/teleConference/{consultationRoomId}")
    public ConsultationRoomConferenceResponse getConsultationRoomTeleConference(@PathVariable("consultationRoomId") int id, Principal principal) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(id, getCurrentOrganisation());
        return consultationRoomMapper.mapForConference(consultationRoom, getCurrentUser(principal));
    }

    @DeleteMapping("/api/consultationRoom/appointment")
    public Integer removeAppointment(@RequestParam("consultationRoomId") int consultationRoomId,
                                     @RequestParam("clientId") int clientId, Principal principal) {
        Appointment appointment = consultationRoomService.removeAppointment(consultationRoomId, clientId, getCurrentOrganisation());
        return appointment.getId();
    }

    @PostMapping("/api/consultationRoom/appointment/next")
    public void moveToNextAppointment(@RequestParam("consultationRoomId") int consultationRoomId, Principal principal) {
        consultationRoomService.moveToNextToken(consultationRoomId, getCurrentUser(principal), getCurrentOrganisation());
    }

    @PostMapping("/api/consultationRoom/appointment/previous")
    public void moveToPreviousAppointment(@RequestParam("consultationRoomId") int consultationRoomId, Principal principal) {
        consultationRoomService.moveToPreviousToken(consultationRoomId, getCurrentUser(principal), getCurrentOrganisation());
    }

    @PostMapping("/api/consultationRoom/appointment/moveDown")
    public void appointmentMoveDown(@RequestParam("consultationRoomId") int consultationRoomId,
                                         @RequestParam("appointmentId") int tokenId, Principal principal) {
        consultationRoomService.appointmentMoveDown(consultationRoomId, tokenId, getCurrentUser(principal), getCurrentOrganisation());
    }

    @PostMapping("/api/consultationRoom/appointment/moveUp")
    public void appointmentMoveUp(@RequestParam("consultationRoomId") int consultationRoomId,
                                         @RequestParam("appointmentId") int tokenId, Principal principal) {
        consultationRoomService.appointmentMoveUp(consultationRoomId, tokenId, getCurrentUser(principal), getCurrentOrganisation());
    }

    @PostMapping("/api/consultationRoom/appointment/setCurrent")
    @Transactional
    public void setCurrentAppointment(@RequestParam("consultationRoomId") int consultationRoomId,
                                         @RequestParam("appointmentId") int appointmentId) {
        consultationRoomService.setCurrentAppointment(consultationRoomId, appointmentId, getCurrentOrganisation());
    }
}
