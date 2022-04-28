package org.opentele.consult.controller.consulationRoom;

import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleRequest;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleResponse;
import org.opentele.consult.controller.BaseController;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomSchedule;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomScheduleUser;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.mapper.consultationRoom.ConsultationRoomScheduleMapper;
import org.opentele.consult.repository.ConsultationRoomScheduleRepository;
import org.opentele.consult.repository.ConsultationRoomScheduleUserRepository;
import org.opentele.consult.repository.framework.Repository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@PreAuthorize("hasRole('User')")
public class ConsultationRoomScheduleController extends BaseController  {
    private final ConsultationRoomScheduleRepository consultationRoomScheduleRepository;
    private final ConsultationRoomScheduleUserRepository consultationRoomScheduleUserRepository;

    @Autowired
    public ConsultationRoomScheduleController(UserService userService, UserSession userSession, ConsultationRoomScheduleRepository consultationRoomScheduleRepository, ConsultationRoomScheduleUserRepository consultationRoomScheduleUserRepository) {
        super(userService, userSession);
        this.consultationRoomScheduleRepository = consultationRoomScheduleRepository;
        this.consultationRoomScheduleUserRepository = consultationRoomScheduleUserRepository;
    }

    @GetMapping(value = "/api/consultationRoomSchedule")
    public List<ConsultationRoomScheduleResponse> getScheduledConsultations() {
        var organisation = getCurrentOrganisation();
        var list = consultationRoomScheduleRepository.findAllByOrganisation(organisation);
        return list.stream().map(ConsultationRoomScheduleMapper::map).collect(Collectors.toList());
    }

    @GetMapping(value = "/api/consultationRoomSchedule/{id}")
    public ConsultationRoomScheduleResponse get(@PathVariable("id") int id) {
        ConsultationRoomSchedule entity = consultationRoomScheduleRepository.findEntity(id, getCurrentOrganisation());
        return ConsultationRoomScheduleMapper.map(entity);
    }

    @RequestMapping(value = "/api/consultationRoomSchedule", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<Integer> put(@RequestBody ConsultationRoomScheduleRequest request) {
        var schedule = Repository.findById(request.getId(), getCurrentOrganisation(), consultationRoomScheduleRepository);
        ConsultationRoomScheduleMapper.map(request, getCurrentOrganisation(), schedule);

        List<Integer> existingProviderIds = schedule.getProviders().stream().map(ConsultationRoomScheduleUser::getId).collect(Collectors.toList());
        Repository.mergeChildren(request.getProviders(), existingProviderIds,
                x -> schedule.removeUser((ConsultationRoomScheduleUser)x),
                x -> schedule.addUser((ConsultationRoomScheduleUser)x),
                x -> consultationRoomScheduleUserRepository.findByConsultationRoomScheduleAndUserIdAndOrganisation(schedule, x, getCurrentOrganisation()),
                x -> {
                    var consultationRoomScheduleUser = new ConsultationRoomScheduleUser();
                    consultationRoomScheduleUser.setUser(userService.getUser(x));
                    consultationRoomScheduleUser.setOrganisation(getCurrentOrganisation());
                    return consultationRoomScheduleUser;
                });
        var saved = consultationRoomScheduleRepository.save(schedule);
        return new ResponseEntity<>(saved.getId(), HttpStatus.OK);
    }
}
