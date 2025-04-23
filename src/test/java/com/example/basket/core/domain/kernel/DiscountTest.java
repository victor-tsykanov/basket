package com.example.basket.core.domain.kernel;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DiscountTest {

    @ParameterizedTest
    @ValueSource(doubles = {0, 35, 100})
    void of_should_createDiscount_when_valueIsValid(double value) {
        var discount = Discount.of(value);

        assertThat(discount.getValue()).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.1, 100.1})
    void of_should_throwException_when_valueIsNotValid(double value) {
        assertThrows(
                ConstraintViolationException.class,
                () -> Discount.of(value)
        );
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "100, 70"
    })
    void apply_should_returnDiscountedPrice_when_priceIsNonNegative(double price, double expectedDiscountedPrice) {
        var discount = Discount.of(30);

        var discountedPrice = discount.apply(price);

        assertThat(discountedPrice).isEqualTo(expectedDiscountedPrice);
    }

    @Test
    void apply_should_throwException_when_priceIsNegative() {
        var discount = Discount.of(30);

        assertThrows(
                IllegalArgumentException.class,
                () -> discount.apply(-1)
        );
    }
}
