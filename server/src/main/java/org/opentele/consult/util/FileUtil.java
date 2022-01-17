package org.opentele.consult.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Locale;

@Component
public class FileUtil {
    private final TemplateEngine templateEngine;
    @Value("${consult.email.location}")
    private String emailLocation;
    private final ResourcePatternResolver resourceResolver;

    @Autowired
    public FileUtil(ResourcePatternResolver resourceResolver) {
        this.resourceResolver = resourceResolver;
        templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(String.format("%s/", emailLocation));
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);
    }

    public String getEmailBody(String emailTemplateName, Context context) {
        Locale locale = LocaleContextHolder.getLocale();
        String language = locale.getLanguage();
        StringWriter stringWriter = new StringWriter();
        templateEngine.process(String.format("%s_%s", emailTemplateName, language), context, stringWriter);
        return stringWriter.toString();
    }

    public void associateEmailAttachments(String emailTemplateName) throws MessagingException, IOException {
        Resource[] resources = resourceResolver.getResources(String.format("classpath:%s/%s/images/*", emailLocation, emailTemplateName));
        for (Resource resource : resources) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setDataHandler(new DataHandler(resource.getURL()));
            String path = resource.getURL().getPath();
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            attachmentPart.setDisposition(Part.ATTACHMENT);
            attachmentPart.setFileName(fileName);
        }
    }
}
