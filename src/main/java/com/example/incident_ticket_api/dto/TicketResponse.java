package com.example.incident_ticket_api.dto;

import com.example.incident_ticket_api.model.TicketPriority;
import com.example.incident_ticket_api.model.TicketStatus;
import java.time.LocalDateTime;

public record TicketResponse(
        Long id,
        String title,
        String description,
        TicketPriority priority,
        TicketStatus status,
        String assignedTo,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
