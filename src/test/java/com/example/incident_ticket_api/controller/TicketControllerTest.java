package com.example.incident_ticket_api.controller;

import com.example.incident_ticket_api.dto.CreateTicketRequest;
import com.example.incident_ticket_api.dto.TicketResponse;
import com.example.incident_ticket_api.dto.UpdateTicketRequest;
import com.example.incident_ticket_api.dto.UpdateTicketStatusRequest;
import com.example.incident_ticket_api.model.TicketPriority;
import com.example.incident_ticket_api.model.TicketStatus;
import com.example.incident_ticket_api.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.validation.Validation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TicketControllerTest {

    private TicketService ticketService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        ticketService = mock(TicketService.class);

        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new TicketController(ticketService))
                .setValidator(validator)
                .build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void shouldCreateTicket() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest(
                "Login not working",
                "A user cannot log in to the internal dashboard.",
                TicketPriority.HIGH,
                "IT Support"
        );

        TicketResponse response = sampleTicketResponse();

        when(ticketService.createTicket(any(CreateTicketRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Login not working"))
                .andExpect(jsonPath("$.status").value("OPEN"))
                .andExpect(jsonPath("$.priority").value("HIGH"));

        verify(ticketService).createTicket(any(CreateTicketRequest.class));
    }

    @Test
    void shouldReturnAllTickets() throws Exception {
        when(ticketService.getAllTickets()).thenReturn(List.of(sampleTicketResponse()));

        mockMvc.perform(get("/api/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Login not working"));

        verify(ticketService).getAllTickets();
    }

    @Test
    void shouldReturnTicketById() throws Exception {
        when(ticketService.getTicketById(1L)).thenReturn(sampleTicketResponse());

        mockMvc.perform(get("/api/tickets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Login not working"));

        verify(ticketService).getTicketById(1L);
    }

    @Test
    void shouldReturnTicketsByStatus() throws Exception {
        when(ticketService.findByStatus(TicketStatus.OPEN))
                .thenReturn(List.of(sampleTicketResponse()));

        mockMvc.perform(get("/api/tickets")
                        .param("status", "OPEN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("OPEN"));

        verify(ticketService).findByStatus(TicketStatus.OPEN);
    }

    @Test
    void shouldReturnTicketsByPriority() throws Exception {
        when(ticketService.findByPriority(TicketPriority.HIGH))
                .thenReturn(List.of(sampleTicketResponse()));

        mockMvc.perform(get("/api/tickets")
                        .param("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].priority").value("HIGH"));

        verify(ticketService).findByPriority(TicketPriority.HIGH);
    }

    @Test
    void shouldReturnTicketsByStatusAndPriority() throws Exception {
        when(ticketService.findByStatusAndPriority(TicketStatus.OPEN, TicketPriority.HIGH))
                .thenReturn(List.of(sampleTicketResponse()));

        mockMvc.perform(get("/api/tickets")
                        .param("status", "OPEN")
                        .param("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("OPEN"))
                .andExpect(jsonPath("$[0].priority").value("HIGH"));

        verify(ticketService).findByStatusAndPriority(TicketStatus.OPEN, TicketPriority.HIGH);
    }

    @Test
    void shouldUpdateTicket() throws Exception {
        UpdateTicketRequest request = new UpdateTicketRequest(
                "Updated title",
                "Updated description",
                TicketPriority.MEDIUM,
                "Backend Team"
        );

        TicketResponse response = new TicketResponse(
                1L,
                "Updated title",
                "Updated description",
                TicketPriority.MEDIUM,
                TicketStatus.OPEN,
                "Backend Team",
                LocalDateTime.of(2026, 5, 4, 10, 0),
                LocalDateTime.of(2026, 5, 4, 11, 0)
        );

        when(ticketService.updateTicket(any(Long.class), any(UpdateTicketRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/api/tickets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated title"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.assignedTo").value("Backend Team"));

        verify(ticketService).updateTicket(any(Long.class), any(UpdateTicketRequest.class));
    }

    @Test
    void shouldUpdateTicketStatus() throws Exception {
        UpdateTicketStatusRequest request = new UpdateTicketStatusRequest(
                TicketStatus.IN_PROGRESS
        );

        TicketResponse response = new TicketResponse(
                1L,
                "Login not working",
                "A user cannot log in to the internal dashboard.",
                TicketPriority.HIGH,
                TicketStatus.IN_PROGRESS,
                "IT Support",
                LocalDateTime.of(2026, 5, 4, 10, 0),
                LocalDateTime.of(2026, 5, 4, 11, 0)
        );

        when(ticketService.updateTicketStatus(any(Long.class), any(UpdateTicketStatusRequest.class)))
                .thenReturn(response);

        mockMvc.perform(patch("/api/tickets/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));

        verify(ticketService).updateTicketStatus(any(Long.class), any(UpdateTicketStatusRequest.class));
    }

    @Test
    void shouldDeleteTicket() throws Exception {
        mockMvc.perform(delete("/api/tickets/1"))
                .andExpect(status().isNoContent());

        verify(ticketService).deleteTicket(1L);
    }

    @Test
    void shouldRejectCreateTicketRequestWithBlankTitle() throws Exception {
        CreateTicketRequest request = new CreateTicketRequest(
                "",
                "A user cannot log in to the internal dashboard.",
                TicketPriority.HIGH,
                "IT Support"
        );

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(ticketService);
    }

    private TicketResponse sampleTicketResponse() {
        return new TicketResponse(
                1L,
                "Login not working",
                "A user cannot log in to the internal dashboard.",
                TicketPriority.HIGH,
                TicketStatus.OPEN,
                "IT Support",
                LocalDateTime.of(2026, 5, 4, 10, 0),
                LocalDateTime.of(2026, 5, 4, 10, 0)
        );
    }
}