package com.example.incident_ticket_api.health;

import com.example.incident_ticket_api.repository.TicketRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * Custom Actuator health check that verifies the ticket repository can reach the database.
 */
@Component
public class TicketRepositoryHealthIndicator implements HealthIndicator {

    private final TicketRepository ticketRepository;

    public TicketRepositoryHealthIndicator(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Uses a cheap count query as a real database signal and reports only safe diagnostic details.
     */
    @Override
    public Health health() {
        try {
            long ticketCount = ticketRepository.count();

            return Health.up()
                    .withDetail("repository", "available")
                    .withDetail("ticketCount", ticketCount)
                    .build();
        } catch (Exception exception) {
            return Health.down()
                    .withDetail("repository", "unavailable")
                    .withDetail("error", exception.getClass().getSimpleName())
                    .build();
        }
    }
}
