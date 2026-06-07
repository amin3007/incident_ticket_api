package com.example.incident_ticket_api.service;

import com.example.incident_ticket_api.dto.CreateTicketRequest;
import com.example.incident_ticket_api.dto.PagedResponse;
import com.example.incident_ticket_api.dto.TicketResponse;
import com.example.incident_ticket_api.dto.UpdateTicketRequest;
import com.example.incident_ticket_api.dto.UpdateTicketStatusRequest;
import com.example.incident_ticket_api.exception.TicketNotFoundException;
import com.example.incident_ticket_api.mapper.TicketMapper;
import com.example.incident_ticket_api.model.Ticket;
import com.example.incident_ticket_api.model.TicketPriority;
import com.example.incident_ticket_api.model.TicketStatus;
import com.example.incident_ticket_api.repository.TicketRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Business layer for ticket use cases.
 * Transactions live here so controllers stay thin and repository calls remain consistent.
 */
@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Creates a ticket through the mapper so default entity behavior and response shaping stay centralized.
     */
    @Transactional
    public TicketResponse createTicket(CreateTicketRequest request) {
        Ticket ticket = TicketMapper.toEntity(request);
        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketMapper.toResponse(savedTicket);
    }

    /**
     * Reads every ticket and maps entities to DTOs before returning them to the web layer.
     */
    @Transactional(readOnly = true)
    public List<TicketResponse> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();

        return TicketMapper.toResponseList(tickets);
    }

    /**
     * Reuses the private lookup helper so missing-ticket behavior is identical across use cases.
     */
    @Transactional(readOnly = true)
    public TicketResponse getTicketById(Long id) {
        Ticket ticket = findTicketEntityById(id);

        return TicketMapper.toResponse(ticket);
    }

    /**
     * Performs a full edit of the mutable ticket fields while preserving workflow status.
     */
    @Transactional
    public TicketResponse updateTicket(Long id, UpdateTicketRequest request) {
        Ticket ticket = findTicketEntityById(id);

        TicketMapper.updateEntity(ticket, request);

        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketMapper.toResponse(savedTicket);
    }

    /**
     * Changes only the workflow status, which keeps status transitions separate from content edits.
     */
    @Transactional
    public TicketResponse updateTicketStatus(Long id, UpdateTicketStatusRequest request) {
        Ticket ticket = findTicketEntityById(id);

        ticket.setStatus(request.status());

        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketMapper.toResponse(savedTicket);
    }

    /**
     * Deletes the loaded entity so a missing id still produces the same not-found exception.
     */
    @Transactional
    public void deleteTicket(Long id) {
        Ticket ticket = findTicketEntityById(id);

        ticketRepository.delete(ticket);
    }

    /**
     * Filters by status through a derived Spring Data query method.
     */
    @Transactional(readOnly = true)
    public List<TicketResponse> findByStatus(TicketStatus status) {
        List<Ticket> tickets = ticketRepository.findByStatus(status);

        return TicketMapper.toResponseList(tickets);
    }

    /**
     * Filters by priority through a derived Spring Data query method.
     */
    @Transactional(readOnly = true)
    public List<TicketResponse> findByPriority(TicketPriority priority) {
        List<Ticket> tickets = ticketRepository.findByPriority(priority);

        return TicketMapper.toResponseList(tickets);
    }

    /**
     * Uses the combined query when both filters are present to avoid filtering in memory.
     */
    @Transactional(readOnly = true)
    public List<TicketResponse> findByStatusAndPriority(TicketStatus status, TicketPriority priority) {
        List<Ticket> tickets = ticketRepository.findByStatusAndPriority(status, priority);

        return TicketMapper.toResponseList(tickets);
    }

    /**
     * Searches tickets with optional filters and pageable sorting.
     * Specifications keep the query flexible without adding repository methods for every filter combination.
     */
    @Transactional(readOnly = true)
    public PagedResponse<TicketResponse> searchTickets(
            TicketStatus status,
            TicketPriority priority,
            String assignee,
            Pageable pageable
    ) {
        Specification<Ticket> specification = Specification.<Ticket>unrestricted()
                .and(hasStatus(status))
                .and(hasPriority(priority))
                .and(hasAssignee(assignee));

        Page<TicketResponse> page = ticketRepository.findAll(specification, pageable)
                .map(TicketMapper::toResponse);

        return PagedResponse.from(page);
    }

    /**
     * Adds a status predicate only when the caller provided a status filter.
     */
    private Specification<Ticket> hasStatus(TicketStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? null : criteriaBuilder.equal(root.get("status"), status);
    }

    /**
     * Adds a priority predicate only when the caller provided a priority filter.
     */
    private Specification<Ticket> hasPriority(TicketPriority priority) {
        return (root, query, criteriaBuilder) ->
                priority == null ? null : criteriaBuilder.equal(root.get("priority"), priority);
    }

    /**
     * Performs a case-insensitive partial match for assignee searches.
     */
    private Specification<Ticket> hasAssignee(String assignee) {
        return (root, query, criteriaBuilder) ->
                assignee == null || assignee.isBlank()
                        ? null
                        : criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("assignedTo")),
                                "%" + assignee.toLowerCase() + "%"
                        );
    }

    /**
     * Centralizes id lookup and translates empty Optional values into the API's domain exception.
     */
    private Ticket findTicketEntityById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));
    }
}
