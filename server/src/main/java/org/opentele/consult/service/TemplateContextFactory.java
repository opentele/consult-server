package org.opentele.consult.service;

import org.opentele.consult.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

@Component
public class TemplateContextFactory {
    private final ApplicationConfig applicationConfig;

    @Autowired
    public TemplateContextFactory(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    public Context createResetPasswordContext(String resetPasswordUrl) {
        Context context = new Context();
        context.setVariable("reset_url", resetPasswordUrl);
        context.setVariable("image_base_path", String.format("%s/%s/%s/", applicationConfig.getServerOrigin(), applicationConfig.getImagesDirectory(), "/resetPassword"));
        return context;
    }
}
