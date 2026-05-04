package com.example.incident_ticket_api.dto;

import com.example.incident_ticket_api.model.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
public record UpdateTicketRequest(

        @NotBlank(message = "Title cannot be blank")
        @Size(max = 120, message = "Title cannot have more than 120 characters")
        String title,

        @NotBlank(message = "Description cannot be blank")
        String description,

        @NotNull(message = "Priority cannot be null")
        TicketPriority priority,

        @Size(max = 120, message = "Assignee cannot have more than 120 characters")
        String assignedTo
) {
}