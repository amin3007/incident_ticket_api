package com.example.incident_ticket_api.dto;

import com.example.incident_ticket_api.model.TicketStatus;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Direct Bean Validation tests for status-only update requests.
 */
class UpdateTicketStatusRequestValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void closeValidatorFactory() {
        validatorFactory.close();
    }

    @Test
    void shouldAcceptValidStatusUpdateRequest() {
        UpdateTicketStatusRequest request = new UpdateTicketStatusRequest(
                TicketStatus.IN_PROGRESS
        );

        Set<ConstraintViolation<UpdateTicketStatusRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldRejectMissingStatus() {
        UpdateTicketStatusRequest request = new UpdateTicketStatusRequest(null);

        Set<ConstraintViolation<UpdateTicketStatusRequest>> violations = validator.validate(request);

        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("status"));
    }
}
