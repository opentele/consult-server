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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "consultation_room_schedule")
public class ConsultationRoomSchedule extends OrganisationalEntity {
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    @Column(nullable = false)
    private String recurrenceRule;

    @OneToMany(cascade = {CascadeType.ALL}, fetch = FetchType.LAZY, mappedBy = "consultationRoomSchedule")
    private Set<ConsultationRoomScheduleUser> providers = new HashSet<>();

    @Column(nullable = false, columnDefinition = "int4 check (total_slots > 0)")
    private int totalSlots;

    public void setRecurrenceRule(String recurrenceRule) {
        this.recurrenceRule = recurrenceRule;
    }

    public List<LocalDate> getNextConsultationDates(LocalDate startDate, LocalDate endDate) {
        try {
            List<LocalDate> dates = new ArrayList<>();
            RecurrenceRule rule = new RecurrenceRule(recurrenceRule);
            RecurrenceRuleIterator it = rule.iterator(getRRDateTime(startDate));
            while (it.hasNext()) {
                DateTime next = it.nextDateTime();
                LocalDate date = getDate(next);
                if (date.isAfter(endDate)) break;

                dates.add(getDate(next));
            }
            return dates;
        } catch (InvalidRecurrenceRuleException e) {
            throw new RuntimeException(e);
        }
    }

    public List<LocalDate> getNextConsultationDates(int numberOfRooms, LocalDate fromDate) {
        try {
            RecurrenceRule rule = new RecurrenceRule(recurrenceRule);
            int max = numberOfRooms;
            RecurrenceRuleIterator it = rule.iterator(getRRDateTime(fromDate));
            List<LocalDate> dates = new ArrayList<>();
            while (it.hasNext() && (!rule.isInfinite() || max-- > 0)) {
                DateTime next = it.nextDateTime();
                dates.add(getDate(next));
            }
            return dates;
        } catch (InvalidRecurrenceRuleException e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDate getDate(DateTime dateTime) {
        return LocalDate.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth());
    }

    private DateTime getRRDateTime(LocalDate fromDate) {
        return new DateTime(fromDate.getYear(), fromDate.getMonthValue(), fromDate.getDayOfMonth());
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setProviders(Set<ConsultationRoomScheduleUser> providers) {
        this.providers = providers;
    }

    public String getRecurrenceRule() {
        return recurrenceRule;
    }

    public Set<ConsultationRoomScheduleUser> getProviders() {
        return providers;
    }

    public int getTotalSlots() {
        return totalSlots;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setTotalSlots(int totalSlots) {
        this.totalSlots = totalSlots;
    }

    public ConsultationRoom createRoomFor(LocalDate date) {
        ConsultationRoom consultationRoom = new ConsultationRoom();
        consultationRoom.setConsultationRoomSchedule(this);
        consultationRoom.setScheduledOn(date);
        consultationRoom.setScheduledStartTime(this.startTime);
        consultationRoom.setScheduledEndTime(this.endTime);
        this.providers.forEach(consultationRoomScheduleUser -> {
            ConsultationRoomUser consultationRoomUser = new ConsultationRoomUser();
            consultationRoomUser.setOrganisation(this.getOrganisation());
            consultationRoomUser.setUser(consultationRoomScheduleUser.getUser());
            consultationRoom.addProvider(consultationRoomUser);
        });
        consultationRoom.setTitle(this.title);
        consultationRoom.setTotalSlots(this.totalSlots);
        consultationRoom.setOrganisation(this.getOrganisation());
        return consultationRoom;
    }
}
