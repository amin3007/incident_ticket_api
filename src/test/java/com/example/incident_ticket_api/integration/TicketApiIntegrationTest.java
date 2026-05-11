package com.example.incident_ticket_api.integration;

import com.example.incident_ticket_api.repository.TicketRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TicketApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TicketRepository ticketRepository;

    @BeforeEach
    void cleanDatabase() {
        ticketRepository.deleteAll();
    }

    @Test
    void shouldRunCompleteTicketLifecycle() throws Exception {
        String createRequest = """
                {
                  "title": "Integration test ticket",
                  "description": "Ticket created during a full API integration test.",
                  "priority": "HIGH",
                  "assignedTo": "Platform Team"
                }
                """;

        String createResponse = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Integration test ticket"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.assignedTo").value("Platform Team"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode createdTicket = objectMapper.readTree(createResponse);
        long ticketId = createdTicket.get("id").asLong();

        mockMvc.perform(get("/api/tickets/{id}", ticketId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketId))
                .andExpect(jsonPath("$.title").value("Integration test ticket"));

        String updateRequest = """
                {
                  "title": "Updated integration test ticket",
                  "description": "Ticket updated during a full API integration test.",
                  "priority": "MEDIUM",
                  "assignedTo": "Backend Team"
                }
                """;

        mockMvc.perform(put("/api/tickets/{id}", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketId))
                .andExpect(jsonPath("$.title").value("Updated integration test ticket"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.assignedTo").value("Backend Team"));

        String statusUpdateRequest = """
                {
                  "status": "IN_PROGRESS"
                }
                """;

        mockMvc.perform(patch("/api/tickets/{id}/status", ticketId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusUpdateRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ticketId))
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        mockMvc.perform(get("/api/tickets")
                        .param("status", "IN_PROGRESS")
                        .param("priority", "MEDIUM"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ticketId))
                .andExpect(jsonPath("$[0].status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$[0].priority").value("MEDIUM"));

        mockMvc.perform(delete("/api/tickets/{id}", ticketId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tickets/{id}", ticketId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void shouldRejectInvalidCreateRequestInFullApplicationContext() throws Exception {
        String invalidRequest = """
                {
                  "title": "",
                  "description": "Invalid request from integration test.",
                  "priority": "HIGH",
                  "assignedTo": "QA Team"
                }
                """;

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("title"));

        assertThat(ticketRepository.count()).isZero();
    }

    @Test
    void shouldRejectInvalidStatusFilterInFullApplicationContext() throws Exception {
        mockMvc.perform(get("/api/tickets")
                        .param("status", "INVALID"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Invalid value for parameter 'status'"));
    }
}