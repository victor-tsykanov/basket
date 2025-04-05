package com.example.basket.core.ports.in.getbasket;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static com.example.basket.common.Validation.requireValid;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class GetBasketQuery {
    UUID id;

    public static GetBasketQuery of(UUID id) {
        return requireValid(new GetBasketQuery(id));
    }
}
