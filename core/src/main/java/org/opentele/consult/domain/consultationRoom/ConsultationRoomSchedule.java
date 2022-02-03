package org.opentele.consult.domain.consultationRoom;

import org.dmfs.rfc5545.DateTime;
import org.dmfs.rfc5545.recur.InvalidRecurrenceRuleException;
import org.dmfs.rfc5545.recur.RecurrenceRule;
import org.dmfs.rfc5545.recur.RecurrenceRuleIterator;
import org.opentele.consult.domain.framework.OrganisationalEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "consultation_room_schedule")
public class ConsultationRoomSchedule extends OrganisationalEntity {
    @Column
    private String title;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;

    @Column
    private String recurrenceRule;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "consultationRoomSchedule")
    private Set<ConsultationRoomScheduleUser> providers = new HashSet<>();

    @Column
    private int totalSlots;

    public void setRecurrenceRule(String recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
    }

    public List<ConsultationRoom> getConsultationRooms(int numberOfRooms, LocalDate fromDate) throws InvalidRecurrenceRuleException {
        RecurrenceRule rule = new RecurrenceRule(recurrenceRule);
        int max = numberOfRooms;
        RecurrenceRuleIterator it = rule.iterator(new DateTime(fromDate.getYear(), fromDate.getMonthValue(), fromDate.getDayOfMonth()));
        List<ConsultationRoom> consultationRooms = new ArrayList<>();
        while (it.hasNext() && (!rule.isInfinite() || max-- > 0)) {
            ConsultationRoom consultationRoom = new ConsultationRoom();
            DateTime next = it.nextDateTime();
            consultationRoom.setScheduledStartAt(LocalDateTime.of(next.getYear(), next.getMonth(), next.getDayOfMonth(), startTime.getHour(), startTime.getMinute()));
            consultationRooms.add(consultationRoom);
        }
        return consultationRooms;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}
