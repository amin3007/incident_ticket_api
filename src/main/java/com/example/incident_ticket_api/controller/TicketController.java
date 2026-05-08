package com.example.incident_ticket_api.controller;

import com.example.incident_ticket_api.dto.CreateTicketRequest;
import com.example.incident_ticket_api.dto.TicketResponse;
import com.example.incident_ticket_api.dto.UpdateTicketRequest;
import com.example.incident_ticket_api.dto.UpdateTicketStatusRequest;
import com.example.incident_ticket_api.model.TicketPriority;
import com.example.incident_ticket_api.model.TicketStatus;
import com.example.incident_ticket_api.service.TicketService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(
            @Valid @RequestBody CreateTicketRequest request
    ) {
        TicketResponse createdTicket = ticketService.createTicket(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdTicket);
    }

    @GetMapping
    public ResponseEntity<List<TicketResponse>> getTickets(
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) TicketPriority priority
    ) {
        List<TicketResponse> tickets;

        if (status != null && priority != null) {
            tickets = ticketService.findByStatusAndPriority(status, priority);
        } else if (status != null) {
            tickets = ticketService.findByStatus(status);
        } else if (priority != null) {
            tickets = ticketService.findByPriority(priority);
        } else {
            tickets = ticketService.getAllTickets();
        }

        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(
            @PathVariable Long id
    ) {
        TicketResponse ticket = ticketService.getTicketById(id);

        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketRequest request
    ) {
        TicketResponse updatedTicket = ticketService.updateTicket(id, request);

        return ResponseEntity.ok(updatedTicket);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateTicketStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketStatusRequest request
    ) {
        TicketResponse updatedTicket = ticketService.updateTicketStatus(id, request);

        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(
            @PathVariable Long id
    ) {
        ticketService.deleteTicket(id);

        return ResponseEntity.noContent().build();
    }
}