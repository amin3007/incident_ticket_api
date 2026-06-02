package com.example.incident_ticket_api.dto;

import com.example.incident_ticket_api.model.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request body for replacing the editable ticket fields.
 * Status is intentionally excluded because it has its own focused PATCH endpoint.
 */
public record UpdateTicketRequest(

        @Schema(description = "Updated ticket title", example = "Login issue resolved partially")
        @NotBlank(message = "Title cannot be blank")
        @Size(max = 120, message = "Title cannot have more than 120 characters")
        String title,

        @Schema(description = "Updated ticket description", example = "The login issue is now limited to one team.")
        @NotBlank(message = "Description cannot be blank")
        String description,

        @Schema(description = "Updated ticket priority", example = "MEDIUM")
        @NotNull(message = "Priority cannot be null")
        TicketPriority priority,

        @Schema(description = "Updated assigned person or team", example = "Backend Team")
        @Size(max = 120, message = "Assignee cannot have more than 120 characters")
        String assignedTo
) {
}
