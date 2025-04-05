package com.example.basket.core.ports.in.addaddress;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static com.example.basket.common.Validation.requireValid;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddAddressCommand {
    UUID basketId;

    @NotBlank
    String country;

    @NotBlank
    String city;

    @NotBlank
    String street;

    @NotBlank
    String house;

    @NotBlank
    String apartment;

    public static AddAddressCommand of(
            UUID basketId,
            String country,
            String city,
            String street,
            String house,
            String apartment
    ) {
        return requireValid(
                new AddAddressCommand(basketId, country, city, street, house, apartment)
        );
    }
}
