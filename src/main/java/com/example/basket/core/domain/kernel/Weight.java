package com.example.basket.core.domain.kernel;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.example.basket.common.Validation.requireValid;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Weight {
    @Min(0)
    int value;

    public static Weight of(int value) {
        return requireValid(new Weight(value));
    }
}
