package com.example.incident_ticket_api.exception;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(

        @Schema(description = "Error timestamp", example = "2026-05-09T12:30:00")
        LocalDateTime timestamp,

        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "HTTP error reason", example = "Bad Request")
        String error,

        @Schema(description = "Error message", example = "Validation failed")
        String message,

        @Schema(description = "Request path", example = "/api/tickets")
        String path,

        @Schema(description = "Field level validation errors")
        List<FieldErrorResponse> fieldErrors
) {
}