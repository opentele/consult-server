package org.opentele.consult.repository;

import org.opentele.consult.domain.consultationRoom.ConsultationRoom;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultationRoomRepository extends PagingAndSortingRepository<ConsultationRoom, Integer> {
    List<ConsultationRoom> findAllByScheduledStartAtAfterAndScheduledEndAtBefore(LocalDateTime from, LocalDateTime to);
}
