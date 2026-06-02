package com.example.incident_ticket_api.repository;

import com.example.incident_ticket_api.model.Ticket;
import com.example.incident_ticket_api.model.TicketPriority;
import com.example.incident_ticket_api.model.TicketStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository slice tests that verify JPA mappings and Spring Data derived queries.
 * The real configured database is used so behavior matches the Docker/PostgreSQL setup.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TicketRepositoryTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void shouldSaveTicket() {
        Ticket ticket = new Ticket(
                "Login not working",
                "A user cannot log in to the internal dashboard.",
                TicketPriority.HIGH,
                "IT Support"
        );

        Ticket savedTicket = ticketRepository.save(ticket);

        assertThat(savedTicket.getId()).isNotNull();
        assertThat(savedTicket.getStatus()).isEqualTo(TicketStatus.OPEN);
        assertThat(savedTicket.getCreatedAt()).isNotNull();
        assertThat(savedTicket.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFindTicketById() {
        Ticket ticket = new Ticket(
                "Dashboard unavailable",
                "The internal dashboard is not reachable.",
                TicketPriority.CRITICAL,
                "Platform Team"
        );

        Ticket savedTicket = ticketRepository.save(ticket);

        Optional<Ticket> result = ticketRepository.findById(savedTicket.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Dashboard unavailable");
    }

    @Test
    void shouldFindTicketsByStatus() {
        Ticket openTicket = new Ticket(
                "Export is slow",
                "The CSV export takes too long.",
                TicketPriority.MEDIUM,
                "Backend Team"
        );

        Ticket resolvedTicket = new Ticket(
                "Typo in help page",
                "There is a typo in an internal help page.",
                TicketPriority.LOW,
                "Content Team"
        );
        resolvedTicket.setStatus(TicketStatus.RESOLVED);

        ticketRepository.save(openTicket);
        ticketRepository.save(resolvedTicket);

        List<Ticket> openTickets = ticketRepository.findByStatus(TicketStatus.OPEN);

        assertThat(openTickets)
                .hasSize(1)
                .extracting(Ticket::getStatus)
                .containsOnly(TicketStatus.OPEN);
    }

    @Test
    void shouldFindTicketsByPriority() {
        Ticket highPriorityTicket = new Ticket(
                "Login not working",
                "Several users cannot log in.",
                TicketPriority.HIGH,
                "IT Support"
        );

        Ticket lowPriorityTicket = new Ticket(
                "Small typo",
                "A typo exists in an internal document.",
                TicketPriority.LOW,
                "Content Team"
        );

        ticketRepository.save(highPriorityTicket);
        ticketRepository.save(lowPriorityTicket);

        List<Ticket> highPriorityTickets = ticketRepository.findByPriority(TicketPriority.HIGH);

        assertThat(highPriorityTickets)
                .hasSize(1)
                .extracting(Ticket::getPriority)
                .containsOnly(TicketPriority.HIGH);
    }

    @Test
    void shouldFindTicketsByAssignedTo() {
        Ticket ticket = new Ticket(
                "VPN connection unstable",
                "The VPN connection drops repeatedly.",
                TicketPriority.MEDIUM,
                "Network Team"
        );

        ticketRepository.save(ticket);

        List<Ticket> result = ticketRepository.findByAssignedTo("Network Team");

        assertThat(result)
                .hasSize(1)
                .extracting(Ticket::getAssignedTo)
                .containsOnly("Network Team");
    }

    @Test
    void shouldFindTicketsByStatusAndPriority() {
        Ticket matchingTicket = new Ticket(
                "Production dashboard unavailable",
                "The production dashboard is not reachable.",
                TicketPriority.CRITICAL,
                "Platform Team"
        );

        Ticket nonMatchingTicket = new Ticket(
                "Export is slow",
                "CSV export takes longer than expected.",
                TicketPriority.MEDIUM,
                "Backend Team"
        );

        ticketRepository.save(matchingTicket);
        ticketRepository.save(nonMatchingTicket);

        List<Ticket> result = ticketRepository.findByStatusAndPriority(
                TicketStatus.OPEN,
                TicketPriority.CRITICAL
        );

        assertThat(result)
                .hasSize(1)
                .extracting(Ticket::getPriority)
                .containsOnly(TicketPriority.CRITICAL);
    }

    @Test
    void shouldDeleteTicketById() {
        Ticket ticket = new Ticket(
                "Temporary test ticket",
                "This ticket should be deleted.",
                TicketPriority.LOW,
                "QA"
        );

        Ticket savedTicket = ticketRepository.save(ticket);

        ticketRepository.deleteById(savedTicket.getId());

        Optional<Ticket> result = ticketRepository.findById(savedTicket.getId());

        assertThat(result).isEmpty();
    }
}
