package org.opentele.consult.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConfig {
    @Value("${consult.email.location}")
    private String emailLocation;

    @Value("${consult.email.sender}")
    private String emailSender;

    @Value("${consult.email.host}")
    private String emailHost;

    @Value("${consult.email.port}")
    private int emailPort;

    @Value("${server.origin}")
    private String serverOrigin;

    @Value("${spring.flyway.enabled}")
    private boolean flywayEnabled;

    @Value("${consult.client.documents.folder}")
    private String attachmentsLocation;

    @Value("${consult.client.documents.max.mb.size}")
    private int clientDocumentsMaxMBSize;

    public String getEmailLocation() {
        return emailLocation;
    }

    public String getEmailSender() {
        return emailSender;
    }

    public String getEmailHost() {
        return emailHost;
    }

    public int getEmailPort() {
        return emailPort;
    }

    public String getServerOrigin() {
        return serverOrigin;
    }

    public String getImagesDirectory() {
        return "static/images";
    }

    public boolean isFlywayEnabled() {
        return flywayEnabled;
    }

    public String getAttachmentsLocation() {
        return attachmentsLocation;
    }

    public void setAttachmentsLocation(String attachmentsLocation) {
        this.attachmentsLocation = attachmentsLocation;
    }

    public long getMaxFileSizeInMegabytes() {
        return clientDocumentsMaxMBSize;
    }
}
