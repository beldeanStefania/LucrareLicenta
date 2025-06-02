package com.orar.Backend.Orar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EntityScan("com.orar.Backend.Orar.model")
@EnableJpaRepositories("com.orar.Backend.Orar.repository")
@EnableScheduling
public class BackendOrarApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendOrarApplication.class, args);
	}

}
