package org.opentele.consult.util;

import org.junit.jupiter.api.Test;
import org.opentele.consult.config.ApplicationConfig;
import org.opentele.consult.framework.FileUtil;
import org.opentele.consult.server.AbstractSpringTest;
import org.opentele.consult.service.TemplateContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FileUtilTest {
    @Autowired
    private FileUtil fileUtil;
    @Autowired
    private TemplateContextFactory templateContextFactory;
    @Autowired
    private ApplicationConfig applicationConfig;

    @Test
    void processTemplate() {
        String resetPasswordOutput = fileUtil.processTemplate("resetPassword", templateContextFactory.createResetPasswordContext("example.com/foo"));
        assertTrue(resetPasswordOutput.contains("/animated_header.gif"));
        assertTrue(resetPasswordOutput.contains(applicationConfig.getServerOrigin()));
    }
}
