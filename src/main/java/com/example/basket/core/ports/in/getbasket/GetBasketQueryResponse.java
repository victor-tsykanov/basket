package com.example.basket.core.ports.in.getbasket;

import java.util.List;
import java.util.UUID;

public record GetBasketQueryResponse(
        UUID id,
        String status,
        String deliveryPeriod,
        Address address,
        List<Item> items
) {
    public record Address(
            String country,
            String city,
            String street,
            String house,
            String apartment
    ) {
    }

    public record Item(
            UUID id,
            UUID goodId,
            int quantity
    ) {
    }
}
