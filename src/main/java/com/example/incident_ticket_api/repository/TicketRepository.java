package com.example.incident_ticket_api.repository;

import com.example.incident_ticket_api.model.Ticket;
import com.example.incident_ticket_api.model.TicketPriority;
import com.example.incident_ticket_api.model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for Ticket persistence.
 * Derived method names keep simple filters declarative without handwritten SQL.
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {

    /**
     * Finds tickets in one workflow state, such as OPEN or RESOLVED.
     */
    List<Ticket> findByStatus(TicketStatus status);

    /**
     * Finds tickets with one urgency level.
     */
    List<Ticket> findByPriority(TicketPriority priority);

    /**
     * Finds tickets assigned to a person or team; currently covered at repository level for future API use.
     */
    List<Ticket> findByAssignedTo(String assignedTo);

    /**
     * Finds tickets that match both filters in the database instead of filtering a larger list in Java.
     */
    List<Ticket> findByStatusAndPriority(TicketStatus status, TicketPriority priority);
}
