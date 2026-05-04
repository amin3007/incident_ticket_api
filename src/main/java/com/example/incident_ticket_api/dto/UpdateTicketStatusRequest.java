package com.example.incident_ticket_api.dto;

import com.example.incident_ticket_api.model.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTicketStatusRequest(

        @NotNull(message = "Status cannot be null")
        TicketStatus status
) {
}
