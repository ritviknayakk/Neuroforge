package com.neuroforge.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class NeuroforgeBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(NeuroforgeBackendApplication.class, args);
	}
}