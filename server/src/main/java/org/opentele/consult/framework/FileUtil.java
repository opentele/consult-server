package org.opentele.consult.framework;

import org.opentele.consult.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.activation.DataHandler;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.UUID;

@Component
public class FileUtil {
    private final TemplateEngine templateEngine;

    private final ResourcePatternResolver resourceResolver;
    private final ApplicationConfig applicationConfig;

    @Autowired
    public FileUtil(ResourcePatternResolver resourceResolver, ApplicationConfig applicationConfig) {
        this.resourceResolver = resourceResolver;
        this.applicationConfig = applicationConfig;
        templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(String.format("%s/", applicationConfig.getEmailLocation()));
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);
    }

    public String processTemplate(String templateName, Context context) {
        Locale locale = LocaleContextHolder.getLocale();
        String language = locale.getLanguage();
        StringWriter stringWriter = new StringWriter();
        templateEngine.process(String.format("%s_%s", templateName, language), context, stringWriter);
        return stringWriter.toString();
    }

    public void associateEmailAttachments(String emailTemplateName, Multipart multipart) throws MessagingException, IOException {
        Resource[] resources = resourceResolver.getResources(String.format("classpath:%s/%s/images/*", applicationConfig.getEmailLocation(), emailTemplateName));
        for (Resource resource : resources) {
            MimeBodyPart attachmentPart = new MimeBodyPart();
            attachmentPart.setDataHandler(new DataHandler(resource.getURL()));
            String path = resource.getURL().getPath();
            String fileName = path.substring(path.lastIndexOf('/') + 1);
            attachmentPart.setDisposition(Part.ATTACHMENT);
            attachmentPart.setFileName(fileName);
            multipart.addBodyPart(attachmentPart);
        }
    }

    public String saveFile(String folder, MultipartFile multipartFile) throws IOException {
        String fileName = UUID.randomUUID().toString();
        File file = new File(String.format("%s/%s", folder, fileName));
        multipartFile.transferTo(file);
        return fileName;
    }

    public long getSizeInMB(MultipartFile multipartFile) {
        return multipartFile.getSize() * 1024 * 1024;
    }
}
