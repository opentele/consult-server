package org.opentele.consult.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Component
public class FileUtil {
    private final TemplateEngine templateEngine;
    @Value("${consult.email.location}")
    private String emailLocation;

    public FileUtil() {
        templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix(String.format("%s/", emailLocation));
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateEngine.setTemplateResolver(templateResolver);
    }

    public String readFile(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    this.getClass().getResourceAsStream(fileName)));
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append(System.getProperty("line.separator"));
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getEmailBody(String emailTemplateName, Context context) {
        Locale locale = LocaleContextHolder.getLocale();
        String language = locale.getLanguage();
        StringWriter stringWriter = new StringWriter();
        templateEngine.process(String.format("%s_%s", emailTemplateName, language), context, stringWriter);
        return stringWriter.toString();
    }
}
