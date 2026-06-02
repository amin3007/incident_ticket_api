package com.example.incident_ticket_api.model;

/**
 * Workflow states a ticket can move through during its lifecycle.
 */
public enum TicketStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED
}
