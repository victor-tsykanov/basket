package com.example.basket.core.ports.in.changeitems;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static com.example.basket.common.Validation.requireValid;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangeItemsCommand {
    UUID basketId;
    UUID buyerId;
    UUID goodId;
    @Min(0)
    int quantity;

    public static ChangeItemsCommand of(UUID basketId, UUID buyerId, UUID goodId, int quantity) {
        return requireValid(
                new ChangeItemsCommand(basketId, buyerId, goodId, quantity)
        );
    }
}
