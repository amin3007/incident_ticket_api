package com.example.incident_ticket_api.health;

import com.example.incident_ticket_api.repository.TicketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TicketRepositoryHealthIndicatorTest {

    @Test
    void shouldReturnUpWhenRepositoryIsAvailable() {
        TicketRepository ticketRepository = mock(TicketRepository.class);
        when(ticketRepository.count()).thenReturn(3L);

        TicketRepositoryHealthIndicator healthIndicator =
                new TicketRepositoryHealthIndicator(ticketRepository);

        Health health = healthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.UP);
        assertThat(health.getDetails()).containsEntry("repository", "available");
        assertThat(health.getDetails()).containsEntry("ticketCount", 3L);
    }

    @Test
    void shouldReturnDownWhenRepositoryThrowsException() {
        TicketRepository ticketRepository = mock(TicketRepository.class);
        when(ticketRepository.count()).thenThrow(new RuntimeException("Database unavailable"));

        TicketRepositoryHealthIndicator healthIndicator =
                new TicketRepositoryHealthIndicator(ticketRepository);

        Health health = healthIndicator.health();

        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
        assertThat(health.getDetails()).containsEntry("repository", "unavailable");
        assertThat(health.getDetails()).containsEntry("error", "RuntimeException");
    }
}