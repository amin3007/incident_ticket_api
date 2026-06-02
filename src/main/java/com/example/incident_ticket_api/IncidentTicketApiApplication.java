package com.example.incident_ticket_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IncidentTicketApiApplication {

	/**
	 * Starts the Spring Boot application and lets Spring auto-configure controllers,
	 * services, repositories, validation, and Actuator based on the classpath.
	 */
	public static void main(String[] args) {
		SpringApplication.run(IncidentTicketApiApplication.class, args);
	}

}
