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

/**
 * Direct Bean Validation tests for create requests.
 * Testing DTO rules here keeps validation expectations clear outside controller tests.
 */
class CreateTicketRequestValidationTest {

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
    void shouldAcceptValidCreateTicketRequest() {
        CreateTicketRequest request = new CreateTicketRequest(
                "Login not working",
                "A user cannot log in to the internal dashboard.",
                TicketPriority.HIGH,
                "IT Support"
        );

        Set<ConstraintViolation<CreateTicketRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldRejectBlankTitle() {
        CreateTicketRequest request = new CreateTicketRequest(
                " ",
                "A user cannot log in to the internal dashboard.",
                TicketPriority.HIGH,
                "IT Support"
        );

        Set<ConstraintViolation<CreateTicketRequest>> violations = validator.validate(request);

        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("title"));
    }

    @Test
    void shouldRejectTooLongTitle() {
        String title = "A".repeat(121);

        CreateTicketRequest request = new CreateTicketRequest(
                title,
                "A user cannot log in to the internal dashboard.",
                TicketPriority.HIGH,
                "IT Support"
        );

        Set<ConstraintViolation<CreateTicketRequest>> violations = validator.validate(request);

        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("title"));
    }

    @Test
    void shouldRejectBlankDescription() {
        CreateTicketRequest request = new CreateTicketRequest(
                "Login not working",
                " ",
                TicketPriority.HIGH,
                "IT Support"
        );

        Set<ConstraintViolation<CreateTicketRequest>> violations = validator.validate(request);

        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("description"));
    }

    @Test
    void shouldRejectMissingPriority() {
        CreateTicketRequest request = new CreateTicketRequest(
                "Login not working",
                "A user cannot log in to the internal dashboard.",
                null,
                "IT Support"
        );

        Set<ConstraintViolation<CreateTicketRequest>> violations = validator.validate(request);

        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("priority"));
    }

    @Test
    void shouldRejectTooLongAssignedTo() {
        String assignedTo = "A".repeat(121);

        CreateTicketRequest request = new CreateTicketRequest(
                "Login not working",
                "A user cannot log in to the internal dashboard.",
                TicketPriority.HIGH,
                assignedTo
        );

        Set<ConstraintViolation<CreateTicketRequest>> violations = validator.validate(request);

        assertThat(violations)
                .anyMatch(violation -> violation.getPropertyPath().toString().equals("assignedTo"));
    }
}
