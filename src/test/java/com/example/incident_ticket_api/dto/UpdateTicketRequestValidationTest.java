package com.example.incident_ticket_api.dto;

import com.example.incident_ticket_api.model.TicketPriority;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UpdateTicketRequestValidationTest {

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
    void shouldAcceptValidUpdateTicketRequest() {
        UpdateTicketRequest request = new UpdateTicketRequest(
                "Updated title",
                "Updated description",
                TicketPriority.MEDIUM,
                "Backend Team"
        );

        Set<ConstraintViolation<UpdateTicketRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldRejectBlankTitle() {
        UpdateTicketRequest request = new UpdateTicketRequest(
                "",
                "Updated description",
                TicketPriority.MEDIUM,
                "Backend Team"
        );

        Set<ConstraintViolation<UpdateTicketRequest>> violations = validator.validate(request);

        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("title"));
    }

    @Test
    void shouldRejectBlankDescription() {
        UpdateTicketRequest request = new UpdateTicketRequest(
                "Updated title",
                "",
                TicketPriority.MEDIUM,
                "Backend Team"
        );

        Set<ConstraintViolation<UpdateTicketRequest>> violations = validator.validate(request);

        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("description"));
    }

    @Test
    void shouldRejectMissingPriority() {
        UpdateTicketRequest request = new UpdateTicketRequest(
                "Updated title",
                "Updated description",
                null,
                "Backend Team"
        );

        Set<ConstraintViolation<UpdateTicketRequest>> violations = validator.validate(request);

        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("priority"));
    }
}