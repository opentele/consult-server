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

    private static final String ConsultationRoomBase = "/api/consultationRoom";

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

    @GetMapping(value = ConsultationRoomBase + "/today")
    public List<ConsultationRoomDetailResponse> getTodayRooms(Principal principal) {
        LocalDate today = LocalDate.now();
        return getConsultationRooms(today, today, principal);
    }

    @GetMapping(value = ConsultationRoomBase + "/past")
    public List<ConsultationRoomDetailResponse> getPastRooms(Principal principal) {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate twoWeeksBack = yesterday.minusWeeks(2);
        return getConsultationRooms(twoWeeksBack, yesterday, principal);
    }

    @GetMapping(value = ConsultationRoomBase + "/future")
    public List<ConsultationRoomDetailResponse> getFutureRooms(Principal principal) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate oneMonthLater = tomorrow.plusMonths(1);
        return getConsultationRooms(tomorrow, oneMonthLater, principal);
    }

    private List<ConsultationRoomDetailResponse> getConsultationRooms(LocalDate startDate, LocalDate endDate, Principal principal) {
        ConsultationRooms consultationRooms = consultationRoomRepository.findAllBetween(startDate, endDate, getCurrentOrganisation());
        return consultationRooms.stream().map(consultationRoom -> consultationRoomMapper.map(consultationRoom, getCurrentUser(principal))).collect(Collectors.toList());
    }

    @GetMapping(value = ConsultationRoomBase + "/{id}")
    public ConsultationRoomContract get(@PathVariable("id") long id) {
        return consultationRoomMapper.map(consultationRoomRepository.findEntity(id, getCurrentOrganisation()));
    }

    @RequestMapping(value = ConsultationRoomBase, method = {RequestMethod.PUT, RequestMethod.POST})
    public Long putPost(@RequestBody ConsultationRoomRequest request) {
        ConsultationRoom consultationRoom = Repository.findByIdOrCreate(request.getId(), getCurrentOrganisation(), consultationRoomRepository, new ConsultationRoom());
        consultationRoom.setOrganisation(getCurrentOrganisation());
        consultationRoom.setTitle(request.getTitle());
        consultationRoom.setTotalSlots(request.getTotalSlots());
        consultationRoom.setScheduledOn(request.getScheduledOn());
        consultationRoom.setScheduledStartTime(request.getScheduledStartTime());
        consultationRoom.setScheduledEndTime(request.getScheduledEndTime());
        List<Long> existingProviderIds = consultationRoom.getProviders().stream().map(x -> x.getUser().getId()).collect(Collectors.toList());
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
        return saved.getId();
    }

    @RequestMapping(value = ConsultationRoomBase + "s", method = {RequestMethod.PUT, RequestMethod.POST})
    public List<Long> putPost(@RequestBody List<ConsultationRoomRequest> requests) {
        return requests.stream().map(this::putPost).collect(Collectors.toList());
    }

    @GetMapping(value = ConsultationRoomBase + "/client")
    public List<ConsultationRoomClientResponse> getClients(@RequestParam(name = "consultationRoomId") long consultationRoomId) {
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

    @GetMapping(ConsultationRoomBase + "/client/search")
    public ClientSearchResults searchResults(@RequestParam("q") String q,
                                             @RequestParam("consultationRoomId") long consultationRoomId) {
        int count = clientRepository.countClientsMatching(q, getCurrentOrganisation(), consultationRoomId);
        List<ClientSearchResponse> clientSearchResponses = clientRepository.getClientsMatching(q, getCurrentOrganisation(), consultationRoomId).stream().map(ClientSearchResponse::from).collect(Collectors.toList());
        return new ClientSearchResults(count, clientSearchResponses);
    }

    @RequestMapping(value = ConsultationRoomBase + "/teleConference", method = {RequestMethod.POST, RequestMethod.PUT})
    public long saveTeleConference(@RequestBody long consultationRoomId) {
        consultationRoomService.setup(consultationRoomId, getCurrentOrganisation());
        return consultationRoomId;
    }

    @GetMapping(ConsultationRoomBase + "/teleConference/{consultationRoomId}")
    public ConsultationRoomConferenceResponse getConsultationRoomTeleConference(@PathVariable("consultationRoomId") long id, Principal principal) {
        ConsultationRoom consultationRoom = consultationRoomRepository.findEntity(id, getCurrentOrganisation());
        return consultationRoomMapper.mapForConference(consultationRoom, getCurrentUser(principal));
    }

    @DeleteMapping(ConsultationRoomBase + "/appointment")
    public Long removeAppointment(@RequestParam("consultationRoomId") long consultationRoomId,
                                  @RequestParam("clientId") long clientId) {
        Appointment appointment = consultationRoomService.removeAppointment(consultationRoomId, clientId, getCurrentOrganisation());
        return appointment.getId();
    }

    @PostMapping(ConsultationRoomBase + "/appointment/next")
    public void moveToNextAppointment(@RequestParam("consultationRoomId") long consultationRoomId, Principal principal) {
        consultationRoomService.moveToNextToken(consultationRoomId, getCurrentUser(principal), getCurrentOrganisation());
    }

    @PostMapping(ConsultationRoomBase + "/appointment/previous")
    public void moveToPreviousAppointment(@RequestParam("consultationRoomId") long consultationRoomId, Principal principal) {
        consultationRoomService.moveToPreviousToken(consultationRoomId, getCurrentUser(principal), getCurrentOrganisation());
    }

    @PostMapping(ConsultationRoomBase + "/appointment/moveDown")
    public boolean appointmentMoveDown(@RequestParam("consultationRoomId") long consultationRoomId,
                                         @RequestParam("appointmentId") int tokenId, Principal principal) {
        consultationRoomService.appointmentMoveDown(consultationRoomId, tokenId, getCurrentUser(principal), getCurrentOrganisation());
        return true;
    }

    @PostMapping(ConsultationRoomBase + "/appointment/moveUp")
    public boolean appointmentMoveUp(@RequestParam("consultationRoomId") long consultationRoomId,
                                         @RequestParam("appointmentId") int tokenId, Principal principal) {
        consultationRoomService.appointmentMoveUp(consultationRoomId, tokenId, getCurrentUser(principal), getCurrentOrganisation());
        return true;
    }

    @PostMapping(ConsultationRoomBase + "/appointment/setCurrent")
    @Transactional
    public boolean setCurrentAppointment(@RequestParam("consultationRoomId") long consultationRoomId,
                                         @RequestParam("appointmentId") int appointmentId) {
        consultationRoomService.setCurrentAppointment(consultationRoomId, appointmentId, getCurrentOrganisation());
        return true;
    }
}
