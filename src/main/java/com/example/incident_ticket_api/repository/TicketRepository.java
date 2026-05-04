package com.example.incident_ticket_api.repository;

import com.example.incident_ticket_api.model.Ticket;
import com.example.incident_ticket_api.model.TicketPriority;
import com.example.incident_ticket_api.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * This interface is a Spring Data JPA repository for the Ticket entity.
 * It extends JpaRepository, which provides basic CRUD operations and pagination support.
 * Additionally, it defines custom query methods to find tickets by status, priority, assigned user, and a combination of status and priority.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByStatus(TicketStatus status);

    List<Ticket> findByPriority(TicketPriority priority);

    List<Ticket> findByAssignedTo(String assignedTo);

    List<Ticket> findByStatusAndPriority(TicketStatus status, TicketPriority priority);
}