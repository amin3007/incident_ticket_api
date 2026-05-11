package com.example.incident_ticket_api.dto;

import com.example.incident_ticket_api.model.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

public record CreateTicketRequest(

        @Schema(description = "Short ticket title", example = "Login not working")
        @NotBlank(message = "Title cannot be blank")
        @Size(max = 120, message = "Title cannot have more than 120 characters")
        String title,

        @Schema(description = "Detailed ticket description", example = "A user cannot log in to the internal dashboard.")
        @NotBlank(message = "Description cannot be blank")
        String description,

        @Schema(description = "Ticket priority", example = "HIGH")
        @NotNull(message = "Priority cannot be null")
        TicketPriority priority,

        @Schema(description = "Person or team assigned to the ticket", example = "IT Support")
        @Size(max = 120, message = "Assignee cannot have more than 120 characters")
        String assignedTo
) {
}