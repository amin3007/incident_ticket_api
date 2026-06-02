package com.example.incident_ticket_api.dto;

import com.example.incident_ticket_api.model.TicketPriority;
import com.example.incident_ticket_api.model.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Public ticket representation returned by the API.
 * Exposing a DTO instead of the JPA entity keeps persistence details out of HTTP responses.
 */
public record TicketResponse(

        @Schema(description = "Unique ticket ID", example = "1")
        Long id,

        @Schema(description = "Ticket title", example = "Login not working")
        String title,

        @Schema(description = "Ticket description", example = "A user cannot log in to the internal dashboard.")
        String description,

        @Schema(description = "Current ticket status", example = "OPEN")
        TicketStatus status,

        @Schema(description = "Ticket priority", example = "HIGH")
        TicketPriority priority,

        @Schema(description = "Person or team assigned to the ticket", example = "IT Support")
        String assignedTo,

        @Schema(description = "Creation timestamp", example = "2026-05-09T10:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp", example = "2026-05-09T10:00:00")
        LocalDateTime updatedAt
) {
}
