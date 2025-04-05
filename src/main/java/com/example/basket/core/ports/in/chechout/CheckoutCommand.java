package com.example.basket.core.ports.in.chechout;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static com.example.basket.common.Validation.requireValid;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckoutCommand {
    UUID basketId;

    public static CheckoutCommand of(UUID basketId) {
        return requireValid(new CheckoutCommand(basketId));
    }
}
