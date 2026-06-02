package com.example.incident_ticket_api.mapper;

import com.example.incident_ticket_api.dto.CreateTicketRequest;
import com.example.incident_ticket_api.dto.TicketResponse;
import com.example.incident_ticket_api.dto.UpdateTicketRequest;
import com.example.incident_ticket_api.model.Ticket;

import java.util.List;

/**
 * Stateless mapper between Ticket entities and API DTOs.
 * A final utility class is enough here because the mapping is simple and has no dependencies.
 */
public final class TicketMapper {

    /**
     * Prevents accidental instantiation of this utility class.
     */
    private TicketMapper() {
    }

    /**
     * Builds a new entity from create input; the entity constructor applies the default OPEN status.
     */
    public static Ticket toEntity(CreateTicketRequest request) {
        return new Ticket(
                request.title(),
                request.description(),
                request.priority(),
                request.assignedTo()
        );
    }

    /**
     * Converts persistence data into the API response contract.
     */
    public static TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                ticket.getAssignedTo(),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt()
        );
    }

    /**
     * Converts repository results to response DTOs while preserving repository order.
     */
    public static List<TicketResponse> toResponseList(List<Ticket> tickets) {
        return tickets.stream()
                .map(TicketMapper::toResponse)
                .toList();
    }

    /**
     * Applies editable fields during a full update and leaves id, status, and timestamps controlled elsewhere.
     */
    public static void updateEntity(Ticket ticket, UpdateTicketRequest request) {
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setPriority(request.priority());
        ticket.setAssignedTo(request.assignedTo());
    }
}
