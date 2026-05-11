package com.example.incident_ticket_api.health;

import com.example.incident_ticket_api.repository.TicketRepository;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class TicketRepositoryHealthIndicator implements HealthIndicator {

    private final TicketRepository ticketRepository;

    public TicketRepositoryHealthIndicator(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

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