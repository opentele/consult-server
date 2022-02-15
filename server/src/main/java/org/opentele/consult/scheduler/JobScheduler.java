package org.opentele.consult.scheduler;

import org.opentele.consult.service.ConsultationRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class JobScheduler {
    private final ConsultationRoomService consultationRoomService;

    @Autowired
    public JobScheduler(ConsultationRoomService consultationRoomService) {
        this.consultationRoomService = consultationRoomService;
    }

    @Scheduled(cron = "${consult.scheduled.room.cron}")
    public void mainJob() {
        consultationRoomService.schedule();
    }
}
