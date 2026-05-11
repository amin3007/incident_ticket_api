package com.example.incident_ticket_api.mapper;

import com.example.incident_ticket_api.dto.CreateTicketRequest;
import com.example.incident_ticket_api.dto.TicketResponse;
import com.example.incident_ticket_api.dto.UpdateTicketRequest;
import com.example.incident_ticket_api.model.Ticket;

import java.util.List;

/**
 * Mapper class to convert between Ticket entity and DTOs.
 */
public final class TicketMapper {

    private TicketMapper() {
    }

    public static Ticket toEntity(CreateTicketRequest request) {
        return new Ticket(
                request.title(),
                request.description(),
                request.priority(),
                request.assignedTo()
        );
    }

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

    public static List<TicketResponse> toResponseList(List<Ticket> tickets) {
        return tickets.stream()
                .map(TicketMapper::toResponse)
                .toList();
    }

    public static void updateEntity(Ticket ticket, UpdateTicketRequest request) {
        ticket.setTitle(request.title());
        ticket.setDescription(request.description());
        ticket.setPriority(request.priority());
        ticket.setAssignedTo(request.assignedTo());
    }
}