package com.example.basket.core.domain.kernel;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.example.basket.common.Validation.requireValid;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Discount {
    @Min(0)
    @Max(100)
    double value;

    public static Discount of(double value) {
        return requireValid(new Discount(value));
    }

    public double apply(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        return price * (1 - value / 100);
    }
}
