package com.example.incident_ticket_api.controller;

import com.example.incident_ticket_api.dto.CreateTicketRequest;
import com.example.incident_ticket_api.dto.PagedResponse;
import com.example.incident_ticket_api.dto.TicketResponse;
import com.example.incident_ticket_api.dto.UpdateTicketRequest;
import com.example.incident_ticket_api.dto.UpdateTicketStatusRequest;
import com.example.incident_ticket_api.exception.ApiErrorResponse;
import com.example.incident_ticket_api.model.TicketPriority;
import com.example.incident_ticket_api.model.TicketStatus;
import com.example.incident_ticket_api.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP layer for ticket operations.
 * The controller keeps request/response concerns here and delegates business rules to TicketService.
 */
@RestController
@RequestMapping("/api/tickets")
@Tag(
        name = "Tickets",
        description = "Operations for managing internal IT incident tickets"
)
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @Operation(
            summary = "Create a new ticket",
            description = "Creates a new internal IT incident ticket. New tickets start with status OPEN."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Ticket created successfully",
                    content = @Content(schema = @Schema(implementation = TicketResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PostMapping
    public ResponseEntity<TicketResponse> createTicket(
            @Valid @RequestBody CreateTicketRequest request
    ) {
        TicketResponse createdTicket = ticketService.createTicket(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdTicket);
    }

    @Operation(
            summary = "Get tickets",
            description = "Returns all tickets or filters tickets by status, priority, or both."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tickets returned successfully with pagination metadata"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid query parameter",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping
    public ResponseEntity<PagedResponse<TicketResponse>> getTickets(
            @Parameter(description = "Optional ticket status filter", example = "OPEN")
            @RequestParam(required = false) TicketStatus status,

            @Parameter(description = "Optional ticket priority filter", example = "HIGH")
            @RequestParam(required = false) TicketPriority priority,

            @Parameter(description = "Optional assignee search filter", example = "IT Support")
            @RequestParam(required = false) String assignee,

            @PageableDefault(size = 20)
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        // Pageable carries page, size, and sort parameters while explicit arguments hold domain filters.
        PagedResponse<TicketResponse> tickets =
                ticketService.searchTickets(status, priority, assignee, pageable);

        return ResponseEntity.ok(tickets);
    }

    @Operation(
            summary = "Get ticket by ID",
            description = "Returns a single ticket by its ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket returned successfully",
                    content = @Content(schema = @Schema(implementation = TicketResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponse> getTicketById(
            @Parameter(description = "Ticket ID", example = "1")
            @PathVariable Long id
    ) {
        TicketResponse ticket = ticketService.getTicketById(id);

        return ResponseEntity.ok(ticket);
    }

    @Operation(
            summary = "Update ticket",
            description = "Updates the editable fields of an existing ticket."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket updated successfully",
                    content = @Content(schema = @Schema(implementation = TicketResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<TicketResponse> updateTicket(
            @Parameter(description = "Ticket ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketRequest request
    ) {
        TicketResponse updatedTicket = ticketService.updateTicket(id, request);

        return ResponseEntity.ok(updatedTicket);
    }

    @Operation(
            summary = "Update ticket status",
            description = "Updates only the status of an existing ticket."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Ticket status updated successfully",
                    content = @Content(schema = @Schema(implementation = TicketResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request body",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<TicketResponse> updateTicketStatus(
            @Parameter(description = "Ticket ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody UpdateTicketStatusRequest request
    ) {
        TicketResponse updatedTicket = ticketService.updateTicketStatus(id, request);

        return ResponseEntity.ok(updatedTicket);
    }

    @Operation(
            summary = "Delete ticket",
            description = "Deletes an existing ticket by its ID."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Ticket deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Ticket not found",
                    content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(
            @Parameter(description = "Ticket ID", example = "1")
            @PathVariable Long id
    ) {
        ticketService.deleteTicket(id);

        return ResponseEntity.noContent().build();
    }
}
