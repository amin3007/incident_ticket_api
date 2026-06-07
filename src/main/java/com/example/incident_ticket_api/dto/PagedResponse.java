package com.example.incident_ticket_api.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Generic response wrapper for endpoints that return paginated data.
 * Keeping pagination metadata next to the content makes API responses easier for clients to consume.
 */
public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last,
        List<String> sort
) {

    /**
     * Converts Spring Data's Page type into a stable API response shape.
     * The API exposes simple sort strings instead of leaking Spring internals to clients.
     */
    public static <T> PagedResponse<T> from(Page<T> page) {
        List<String> sort = page.getSort()
                .stream()
                .map(order -> order.getProperty() + "," + order.getDirection().name().toLowerCase())
                .toList();

        return new PagedResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                sort
        );
    }
}
