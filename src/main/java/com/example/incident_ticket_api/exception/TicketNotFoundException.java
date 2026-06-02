package com.example.incident_ticket_api.exception;

/**
 * Domain-specific exception used when a ticket lookup fails.
 */
public class TicketNotFoundException extends RuntimeException {

    /**
     * Keeps the not-found message consistent across service, controller, and tests.
     */
    public TicketNotFoundException(Long id) {
        super("Ticket with id " + id + " was not found");
    }
}
