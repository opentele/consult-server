package org.opentele.consult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@SpringBootApplication
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
