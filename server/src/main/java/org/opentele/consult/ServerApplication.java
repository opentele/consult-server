package org.opentele.consult;

import org.opentele.consult.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.Properties;

@SpringBootApplication
@EnableJpaAuditing
public class ServerApplication implements WebMvcConfigurer, CommandLineRunner {
    private final ApplicationConfig applicationConfig;

    @Autowired
	public ServerApplication(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean
    public JavaMailSender javaMailService() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(applicationConfig.getEmailHost());
        javaMailSender.setPort(applicationConfig.getEmailPort());
        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "false");
        properties.setProperty("mail.smtp.starttls.enable", "false");
        properties.setProperty("mail.debug", "false");
        return properties;
    }

	public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ServerApplication.class, args);
        if ("false".equals(run.getEnvironment().getProperty("spring.flyway.enabled")))
            run.close();
	}

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations(String.format("file:///%s/", new File("static").getAbsolutePath()));
    }

    @Override
    public void run(String... args) throws Exception {
        if (!applicationConfig.isFlywayEnabled())
            System.exit(0);
    }
}
