package org.opentele.consult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@SpringBootApplication
@EnableJpaAuditing
public class ServerApplication {
    private Environment environment;

    @Autowired
	public ServerApplication(Environment environment) {
        this.environment = environment;
    }

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}
}
