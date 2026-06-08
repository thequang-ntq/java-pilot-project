package com.ntq.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring Boot Application
 *
 * @author Quang
 * @since 2026-04-29
 */
@SpringBootApplication
public class PilotProjectBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(PilotProjectBackendApplication.class, args);
	}
}
