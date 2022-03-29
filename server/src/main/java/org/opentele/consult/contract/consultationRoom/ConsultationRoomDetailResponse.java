package org.opentele.consult.contract.consultationRoom;

public class ConsultationRoomDetailResponse extends ConsultationRoomContract {
    private int numberOfClients;
    private int numberOfUserClients;
    private int numberOfClientsPending;
    private int numberOfUserClientsPending;
    private String nextClient;
    private String activeTeleConferenceId;
    private int currentClientId;

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

    public String getActiveTeleConferenceId() {
        return activeTeleConferenceId;
    }

    public void setActiveTeleConferenceId(String activeTeleConferenceId) {
        this.activeTeleConferenceId = activeTeleConferenceId;
    }

    public int getCurrentClientId() {
        return currentClientId;
    }

    public void setCurrentClientId(int currentClientId) {
        this.currentClientId = currentClientId;
    }
}
