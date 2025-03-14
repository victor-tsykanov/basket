package com.example.basket.common;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static com.example.basket.common.Validation.requireValid;

class ValidationTest {

    record Person(
            @Min(0) int age,
            @NotBlank String name
    ) {
    }

    @Test
    void requireValid_should_returnValue_when_valueIsValid() {
        var person = new Person(30, "Bob");

        var validatedPerson = requireValid(person);

        assertThat(validatedPerson).isSameAs(person);
    }

    @Test
    void requireValid_should_throwException_when_valueIsNotValid() {
        @SuppressWarnings("DataFlowIssue")
        var person = new Person(-1, "");

        var exception = assertThrows(
                ConstraintViolationException.class,
                () -> requireValid(person)
        );

        assertThat(exception.getConstraintViolations()).hasSize(2);
    }
}
