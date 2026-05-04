package com.example.incident_ticket_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is a simple REST controller that provides a ping endpoint.
 * When a GET request is made to /api/ping, it will return the string "pong".
 * This can be used to check if the server is running and responsive.
 */
@RestController //indicates that this class is a REST controller, which means it will handle HTTP requests and return responses in a RESTful manner
public class PingController {

    @GetMapping("/api/ping") //maps HTTP GET requests to /api/ping to this method
    public String ping() {
        return "pong";
    }
}
