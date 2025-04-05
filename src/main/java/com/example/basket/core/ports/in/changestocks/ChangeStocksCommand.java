package com.example.basket.core.ports.in.changestocks;

import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static com.example.basket.common.Validation.requireValid;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ChangeStocksCommand {
    UUID goodId;
    @Min(0)
    int quantity;

    public static ChangeStocksCommand of(UUID goodId, int quantity) {
        return requireValid(new ChangeStocksCommand(goodId, quantity));
    }
}
