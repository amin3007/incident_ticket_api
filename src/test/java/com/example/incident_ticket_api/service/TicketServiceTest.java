package com.example.incident_ticket_api.service;

import com.example.incident_ticket_api.dto.CreateTicketRequest;
import com.example.incident_ticket_api.dto.TicketResponse;
import com.example.incident_ticket_api.dto.UpdateTicketRequest;
import com.example.incident_ticket_api.dto.UpdateTicketStatusRequest;
import com.example.incident_ticket_api.exception.TicketNotFoundException;
import com.example.incident_ticket_api.model.Ticket;
import com.example.incident_ticket_api.model.TicketPriority;
import com.example.incident_ticket_api.model.TicketStatus;
import com.example.incident_ticket_api.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Service unit tests with a mocked repository.
 * This keeps business logic checks fast and independent of Spring MVC or database wiring.
 */
class TicketServiceTest {

    private TicketRepository ticketRepository;
    private TicketService ticketService;

    @BeforeEach
    void setUp() {
        ticketRepository = mock(TicketRepository.class);
        ticketService = new TicketService(ticketRepository);
    }

    @Test
    void shouldCreateTicket() {
        CreateTicketRequest request = new CreateTicketRequest(
                "Login not working",
                "A user cannot log in to the internal dashboard.",
                TicketPriority.HIGH,
                "IT Support"
        );

        Ticket savedTicket = createTicketWithId(
                1L,
                "Login not working",
                "A user cannot log in to the internal dashboard.",
                TicketPriority.HIGH,
                "IT Support"
        );

        when(ticketRepository.save(any(Ticket.class))).thenReturn(savedTicket);

        TicketResponse response = ticketService.createTicket(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Login not working");
        assertThat(response.priority()).isEqualTo(TicketPriority.HIGH);
        assertThat(response.status()).isEqualTo(TicketStatus.OPEN);

        verify(ticketRepository).save(any(Ticket.class));
    }

    @Test
    void shouldReturnAllTickets() {
        Ticket firstTicket = createTicketWithId(
                1L,
                "Login not working",
                "A user cannot log in.",
                TicketPriority.HIGH,
                "IT Support"
        );

        Ticket secondTicket = createTicketWithId(
                2L,
                "Export is slow",
                "CSV export takes too long.",
                TicketPriority.MEDIUM,
                "Backend Team"
        );

        when(ticketRepository.findAll()).thenReturn(List.of(firstTicket, secondTicket));

        List<TicketResponse> responses = ticketService.getAllTickets();

        assertThat(responses).hasSize(2);
        assertThat(responses)
                .extracting(TicketResponse::title)
                .containsExactly("Login not working", "Export is slow");

        verify(ticketRepository).findAll();
    }

    @Test
    void shouldReturnTicketById() {
        Ticket ticket = createTicketWithId(
                1L,
                "Dashboard unavailable",
                "The internal dashboard is not reachable.",
                TicketPriority.CRITICAL,
                "Platform Team"
        );

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticket));

        TicketResponse response = ticketService.getTicketById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Dashboard unavailable");
        assertThat(response.priority()).isEqualTo(TicketPriority.CRITICAL);

        verify(ticketRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTicketByIdDoesNotExist() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.getTicketById(99L)
        );

        verify(ticketRepository).findById(99L);
    }

    @Test
    void shouldUpdateTicket() {
        Ticket existingTicket = createTicketWithId(
                1L,
                "Old title",
                "Old description",
                TicketPriority.LOW,
                "Old Team"
        );

        UpdateTicketRequest request = new UpdateTicketRequest(
                "New title",
                "New description",
                TicketPriority.HIGH,
                "New Team"
        );

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(existingTicket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TicketResponse response = ticketService.updateTicket(1L, request);

        assertThat(response.title()).isEqualTo("New title");
        assertThat(response.description()).isEqualTo("New description");
        assertThat(response.priority()).isEqualTo(TicketPriority.HIGH);
        assertThat(response.assignedTo()).isEqualTo("New Team");

        verify(ticketRepository).findById(1L);
        verify(ticketRepository).save(existingTicket);
    }

    @Test
    void shouldUpdateTicketStatus() {
        Ticket existingTicket = createTicketWithId(
                1L,
                "Login not working",
                "A user cannot log in.",
                TicketPriority.HIGH,
                "IT Support"
        );

        UpdateTicketStatusRequest request = new UpdateTicketStatusRequest(
                TicketStatus.IN_PROGRESS
        );

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(existingTicket));
        when(ticketRepository.save(any(Ticket.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TicketResponse response = ticketService.updateTicketStatus(1L, request);

        assertThat(response.status()).isEqualTo(TicketStatus.IN_PROGRESS);

        verify(ticketRepository).findById(1L);
        verify(ticketRepository).save(existingTicket);
    }

    @Test
    void shouldDeleteTicket() {
        Ticket existingTicket = createTicketWithId(
                1L,
                "Temporary ticket",
                "This ticket should be deleted.",
                TicketPriority.LOW,
                "QA"
        );

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(existingTicket));

        ticketService.deleteTicket(1L);

        verify(ticketRepository).findById(1L);
        verify(ticketRepository).delete(existingTicket);
    }

    @Test
    void shouldFindTicketsByStatus() {
        Ticket ticket = createTicketWithId(
                1L,
                "Login not working",
                "A user cannot log in.",
                TicketPriority.HIGH,
                "IT Support"
        );

        when(ticketRepository.findByStatus(TicketStatus.OPEN)).thenReturn(List.of(ticket));

        List<TicketResponse> responses = ticketService.findByStatus(TicketStatus.OPEN);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).status()).isEqualTo(TicketStatus.OPEN);

        verify(ticketRepository).findByStatus(TicketStatus.OPEN);
    }

    @Test
    void shouldFindTicketsByPriority() {
        Ticket ticket = createTicketWithId(
                1L,
                "Dashboard unavailable",
                "The internal dashboard is not reachable.",
                TicketPriority.CRITICAL,
                "Platform Team"
        );

        when(ticketRepository.findByPriority(TicketPriority.CRITICAL)).thenReturn(List.of(ticket));

        List<TicketResponse> responses = ticketService.findByPriority(TicketPriority.CRITICAL);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).priority()).isEqualTo(TicketPriority.CRITICAL);

        verify(ticketRepository).findByPriority(TicketPriority.CRITICAL);
    }

    @Test
    void shouldFindTicketsByStatusAndPriority() {
        Ticket ticket = createTicketWithId(
                1L,
                "Dashboard unavailable",
                "The internal dashboard is not reachable.",
                TicketPriority.CRITICAL,
                "Platform Team"
        );

        when(ticketRepository.findByStatusAndPriority(
                TicketStatus.OPEN,
                TicketPriority.CRITICAL
        )).thenReturn(List.of(ticket));

        List<TicketResponse> responses = ticketService.findByStatusAndPriority(
                TicketStatus.OPEN,
                TicketPriority.CRITICAL
        );

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).status()).isEqualTo(TicketStatus.OPEN);
        assertThat(responses.get(0).priority()).isEqualTo(TicketPriority.CRITICAL);

        verify(ticketRepository).findByStatusAndPriority(
                TicketStatus.OPEN,
                TicketPriority.CRITICAL
        );
    }

    /**
     * Assigns ids without saving through JPA so service tests can focus on service behavior.
     */
    private Ticket createTicketWithId(
            Long id,
            String title,
            String description,
            TicketPriority priority,
            String assignedTo
    ) {
        Ticket ticket = new Ticket(title, description, priority, assignedTo);
        ReflectionTestUtils.setField(ticket, "id", id);

        return ticket;
    }
}
