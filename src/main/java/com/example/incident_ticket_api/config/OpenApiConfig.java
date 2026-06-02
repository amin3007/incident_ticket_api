package com.example.incident_ticket_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central OpenAPI metadata so Swagger UI shows a clear project name, version, and purpose.
 */
@Configuration
public class OpenApiConfig {

    /**
     * Builds the OpenAPI description used by springdoc when serving /swagger-ui.html and /v3/api-docs.
     */
    @Bean
    public OpenAPI incidentTicketApiOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Incident Ticket API")
                        .version("1.0.0")
                        .description("REST API for managing internal IT incident tickets."));
    }
}
