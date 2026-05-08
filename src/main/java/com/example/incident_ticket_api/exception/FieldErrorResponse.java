package com.example.incident_ticket_api.exception;

public record FieldErrorResponse(
        String field,
        String message
) {
}