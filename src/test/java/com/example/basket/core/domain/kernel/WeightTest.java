package com.example.basket.core.domain.kernel;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WeightTest {

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void of_should_createWight_when_valueIsNonNegative(int value) {
        var weight = Weight.of(value);

        assertThat(weight.getValue()).isEqualTo(value);
    }

    @Test
    void of_should_throwException_when_valueIsNegative() {
        assertThrows(
                ConstraintViolationException.class,
                () -> Weight.of(-1)
        );
    }
}
