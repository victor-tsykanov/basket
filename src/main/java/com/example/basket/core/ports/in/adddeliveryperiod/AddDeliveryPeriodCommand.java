package com.example.basket.core.ports.in.adddeliveryperiod;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.UUID;

import static com.example.basket.common.Validation.requireValid;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AddDeliveryPeriodCommand {
    UUID basketId;

    @NotBlank
    String deliveryPeriod;

    public static AddDeliveryPeriodCommand of(UUID basketId, String deliveryPeriod) {
        return requireValid(new AddDeliveryPeriodCommand(basketId, deliveryPeriod));
    }
}
