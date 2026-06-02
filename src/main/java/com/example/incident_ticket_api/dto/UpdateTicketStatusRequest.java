package com.example.incident_ticket_api.dto;

import com.example.incident_ticket_api.model.TicketStatus;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request body for status-only updates.
 * A separate DTO prevents full-ticket edits when only workflow state should change.
 */
public record UpdateTicketStatusRequest(

        @Schema(description = "New ticket status", example = "IN_PROGRESS")
        @NotNull(message = "Status cannot be null")
        TicketStatus status
) {
}
