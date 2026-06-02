package com.example.incident_ticket_api.exception;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * One validation problem for a single request field.
 */
public record FieldErrorResponse(

        @Schema(description = "Invalid field name", example = "title")
        String field,

        @Schema(description = "Validation error message", example = "Title must not be blank")
        String message
) {
}
