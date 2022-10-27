package org.opentele.consult.scheduler;

import org.opentele.consult.service.ConsultationRoomService;
import org.opentele.consult.service.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class JobScheduler {
    private final ConsultationRoomService consultationRoomService;
    private final static Logger logger = LoggerFactory.getLogger(JobScheduler.class);

    @Autowired
    public JobScheduler(ConsultationRoomService consultationRoomService) {
        this.consultationRoomService = consultationRoomService;
    }

    @Scheduled(cron = "${consult.schedule.room.cron}")
    public void mainJob() {
        SecurityService.setupScheduledJobSecurityContext();
        logger.info("Started scheduling rooms in background");
        int numberOfRoomsScheduled = consultationRoomService.schedule(30);
        logger.info("Room scheduling completed. Scheduled {} rooms", numberOfRoomsScheduled);
    }
}
