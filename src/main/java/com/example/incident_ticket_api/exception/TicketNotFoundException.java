package com.example.incident_ticket_api.exception;

public class TicketNotFoundException extends RuntimeException {

    public TicketNotFoundException(Long id) {
        super("Ticket with id " + id + " was not found");
    }
}
