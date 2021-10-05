package org.opentele.consult.contract;

import com.fasterxml.jackson.annotation.JsonInclude;

public class ApplicationStatus {
    private String message;
    private String errorMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public static ApplicationStatus createSuccess(String message) {
        ApplicationStatus applicationStatus = new ApplicationStatus();
        applicationStatus.setMessage(message);
        return applicationStatus;
    }

    public static ApplicationStatus createFailure(String message) {
        ApplicationStatus applicationStatus = new ApplicationStatus();
        applicationStatus.setErrorMessage(message);
        return applicationStatus;
    }
}
