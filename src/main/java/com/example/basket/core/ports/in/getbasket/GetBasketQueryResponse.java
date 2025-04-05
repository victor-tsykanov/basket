package com.example.basket.core.ports.in.getbasket;

import java.util.UUID;

public record GetBasketQueryResponse(
        UUID id,
        String status,
        Address address
) {
    public record Address(
            String country,
            String street,
            String house,
            String apartment
    ) {
    }
}
