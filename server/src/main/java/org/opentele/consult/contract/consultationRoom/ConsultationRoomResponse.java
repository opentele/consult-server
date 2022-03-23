package org.opentele.consult.contract.consultationRoom;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultationRoomResponse extends ConsultationRoomScheduleContract {
    private LocalDate scheduledOn;
    private LocalTime scheduledStartTime;
    private LocalTime scheduledEndTime;
    private int scheduleId;
    private int numberOfClients;
    private int numberOfUserClients;
    private int numberOfClientsPending;
    private int numberOfUserClientsPending;
    private String nextClient;

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public LocalDate getScheduledOn() {
        return scheduledOn;
    }

    public void setScheduledOn(LocalDate scheduledOn) {
        this.scheduledOn = scheduledOn;
    }

    public LocalTime getScheduledStartTime() {
        return scheduledStartTime;
    }

    public void setScheduledStartTime(LocalTime scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }

    public LocalTime getScheduledEndTime() {
        return scheduledEndTime;
    }

    public void setScheduledEndTime(LocalTime scheduledEndTime) {
        this.scheduledEndTime = scheduledEndTime;
    }

    public int getNumberOfClients() {
        return numberOfClients;
    }

    public void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public int getNumberOfUserClients() {
        return numberOfUserClients;
    }

    public void setNumberOfUserClients(int numberOfUserClients) {
        this.numberOfUserClients = numberOfUserClients;
    }

    public String getNextClient() {
        return nextClient;
    }

    public void setNextClient(String nextClient) {
        this.nextClient = nextClient;
    }

    public int getNumberOfClientsPending() {
        return numberOfClientsPending;
    }

    public void setNumberOfClientsPending(int numberOfClientsPending) {
        this.numberOfClientsPending = numberOfClientsPending;
    }

    public int getNumberOfUserClientsPending() {
        return numberOfUserClientsPending;
    }

    public void setNumberOfUserClientsPending(int numberOfUserClientsPending) {
        this.numberOfUserClientsPending = numberOfUserClientsPending;
    }
}
