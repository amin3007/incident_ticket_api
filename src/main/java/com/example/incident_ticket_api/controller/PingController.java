package com.example.incident_ticket_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Lightweight liveness endpoint for quick manual checks outside Actuator.
 */
@RestController
public class PingController {

    /**
     * Returns a fixed response so callers can confirm that the HTTP server is reachable.
     */
    @GetMapping("/api/ping")
    public String ping() {
        return "pong";
    }
}
