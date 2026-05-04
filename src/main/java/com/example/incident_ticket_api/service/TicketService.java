package com.example.incident_ticket_api.service;

import com.example.incident_ticket_api.dto.CreateTicketRequest;
import com.example.incident_ticket_api.dto.TicketResponse;
import com.example.incident_ticket_api.dto.UpdateTicketRequest;
import com.example.incident_ticket_api.dto.UpdateTicketStatusRequest;
import com.example.incident_ticket_api.exception.TicketNotFoundException;
import com.example.incident_ticket_api.mapper.TicketMapper;
import com.example.incident_ticket_api.model.Ticket;
import com.example.incident_ticket_api.model.TicketPriority;
import com.example.incident_ticket_api.model.TicketStatus;
import com.example.incident_ticket_api.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service class for managing tickets. It provides methods for creating, retrieving, updating, and deleting tickets,
 * as well as filtering tickets by status and priority.
 */
@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public TicketResponse createTicket(CreateTicketRequest request) {
        Ticket ticket = TicketMapper.toEntity(request);
        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketMapper.toResponse(savedTicket);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();

        return TicketMapper.toResponseList(tickets);
    }

    @Transactional(readOnly = true)
    public TicketResponse getTicketById(Long id) {
        Ticket ticket = findTicketEntityById(id);

        return TicketMapper.toResponse(ticket);
    }

    @Transactional
    public TicketResponse updateTicket(Long id, UpdateTicketRequest request) {
        Ticket ticket = findTicketEntityById(id);

        TicketMapper.updateEntity(ticket, request);

        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketMapper.toResponse(savedTicket);
    }

    @Transactional
    public TicketResponse updateTicketStatus(Long id, UpdateTicketStatusRequest request) {
        Ticket ticket = findTicketEntityById(id);

        ticket.setStatus(request.status());

        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketMapper.toResponse(savedTicket);
    }

    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = findTicketEntityById(id);

        ticketRepository.delete(ticket);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> findByStatus(TicketStatus status) {
        List<Ticket> tickets = ticketRepository.findByStatus(status);

        return TicketMapper.toResponseList(tickets);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> findByPriority(TicketPriority priority) {
        List<Ticket> tickets = ticketRepository.findByPriority(priority);

        return TicketMapper.toResponseList(tickets);
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> findByStatusAndPriority(TicketStatus status, TicketPriority priority) {
        List<Ticket> tickets = ticketRepository.findByStatusAndPriority(status, priority);

        return TicketMapper.toResponseList(tickets);
    }

    private Ticket findTicketEntityById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }
}