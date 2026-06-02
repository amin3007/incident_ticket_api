package com.example.incident_ticket_api.mapper;

import com.example.incident_ticket_api.dto.CreateTicketRequest;
import com.example.incident_ticket_api.dto.TicketResponse;
import com.example.incident_ticket_api.dto.UpdateTicketRequest;
import com.example.incident_ticket_api.model.Ticket;
import com.example.incident_ticket_api.model.TicketPriority;
import com.example.incident_ticket_api.model.TicketStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for mapping code so DTO/entity conversion can change safely without web or database setup.
 */
class TicketMapperTest {

    @Test
    void shouldMapCreateRequestToEntity() {
        CreateTicketRequest request = new CreateTicketRequest(
                "Login not working",
                "A user cannot log in to the internal dashboard.",
                TicketPriority.HIGH,
                "IT Support"
        );

        Ticket ticket = TicketMapper.toEntity(request);

        assertThat(ticket.getTitle()).isEqualTo("Login not working");
        assertThat(ticket.getDescription()).isEqualTo("A user cannot log in to the internal dashboard.");
        assertThat(ticket.getPriority()).isEqualTo(TicketPriority.HIGH);
        assertThat(ticket.getAssignedTo()).isEqualTo("IT Support");
        assertThat(ticket.getStatus()).isEqualTo(TicketStatus.OPEN);
    }

    @Test
    void shouldMapEntityToResponse() {
        Ticket ticket = new Ticket(
                "Dashboard unavailable",
                "The internal dashboard is not reachable.",
                TicketPriority.CRITICAL,
                "Platform Team"
        );

        TicketResponse response = TicketMapper.toResponse(ticket);

        assertThat(response.title()).isEqualTo("Dashboard unavailable");
        assertThat(response.description()).isEqualTo("The internal dashboard is not reachable.");
        assertThat(response.priority()).isEqualTo(TicketPriority.CRITICAL);
        assertThat(response.assignedTo()).isEqualTo("Platform Team");
        assertThat(response.status()).isEqualTo(TicketStatus.OPEN);
    }

    @Test
    void shouldUpdateEntityFromUpdateRequest() {
        Ticket ticket = new Ticket(
                "Old title",
                "Old description",
                TicketPriority.LOW,
                "Old Team"
        );

        UpdateTicketRequest request = new UpdateTicketRequest(
                "New title",
                "New description",
                TicketPriority.HIGH,
                "New Team"
        );

        TicketMapper.updateEntity(ticket, request);

        assertThat(ticket.getTitle()).isEqualTo("New title");
        assertThat(ticket.getDescription()).isEqualTo("New description");
        assertThat(ticket.getPriority()).isEqualTo(TicketPriority.HIGH);
        assertThat(ticket.getAssignedTo()).isEqualTo("New Team");
    }
}
