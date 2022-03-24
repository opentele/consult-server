package org.opentele.consult.controller.consulationRoom;

import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleRequest;
import org.opentele.consult.contract.consultationRoom.ConsultationRoomScheduleResponse;
import org.opentele.consult.controller.BaseController;
import org.opentele.consult.domain.consultationRoom.ConsultationRoomScheduleUser;
import org.opentele.consult.framework.UserSession;
import org.opentele.consult.mapper.consultationRoom.ConsultationRoomScheduleMapper;
import org.opentele.consult.repository.ConsultationRoomScheduleRepository;
import org.opentele.consult.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ConsultationRoomScheduleController extends BaseController  {
    private final ConsultationRoomScheduleRepository consultationRoomScheduleRepository;

    @Autowired
    public ConsultationRoomScheduleController(UserService userService, UserSession userSession, ConsultationRoomScheduleRepository consultationRoomScheduleRepository) {
        super(userService, userSession);
        this.consultationRoomScheduleRepository = consultationRoomScheduleRepository;
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
    public ResponseEntity<Integer> put(@RequestBody ConsultationRoomScheduleRequest request) {
        var consultationRoomSchedule = ConsultationRoomScheduleMapper.mapNew(request, getCurrentOrganisation());
        var consultationRoomScheduleUsers = request.getProviders().stream().map(userId -> {
            var consultationRoomScheduleUser = new ConsultationRoomScheduleUser();
            consultationRoomScheduleUser.setConsultationRoomSchedule(consultationRoomSchedule);
            consultationRoomScheduleUser.setUser(getUser(userId));
            consultationRoomScheduleUser.setOrganisation(getCurrentOrganisation());
            return consultationRoomScheduleUser;
        }).collect(Collectors.toSet());
        consultationRoomSchedule.setProviders(consultationRoomScheduleUsers);
        var saved = consultationRoomScheduleRepository.save(consultationRoomSchedule);
        return new ResponseEntity<>(saved.getId(), HttpStatus.OK);
    }
}
