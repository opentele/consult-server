package org.opentele.consult.server;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@EntityScan(basePackages = {"org.opentele.consult.domain"})
@EnableJpaRepositories(basePackages = "org.opentele.consult.repository")
@ComponentScan(basePackages = "org.opentele.consult")
public abstract class AbstractSpringTest {
}
