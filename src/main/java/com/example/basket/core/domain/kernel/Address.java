package com.example.basket.core.domain.kernel;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import static com.example.basket.common.Validation.requireValid;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address {
    @NotBlank
    String country;
    @NotBlank
    String street;
    @NotBlank
    String house;
    @NotBlank
    String apartment;

    public static Address of(String country, String street, String house, String apartment) {
        return requireValid(new Address(country, street, house, apartment));
    }
}
