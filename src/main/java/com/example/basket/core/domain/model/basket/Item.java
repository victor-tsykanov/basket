package com.example.basket.core.domain.model.basket;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import com.example.basket.core.domain.model.good.Good;

import java.util.UUID;

import static com.example.basket.common.Validation.requireValid;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Item {
    @NotNull
    private final UUID id;

    @NotNull
    private final UUID goodId;

    @NotBlank
    private final String title;

    @NotBlank
    private final String description;

    @Min(0)
    private final double price;

    @Min(0)
    private int quantity;

    static Item of(Good good, int quantity) {
        return requireValid(
                new Item(
                        UUID.randomUUID(),
                        good.getId(),
                        good.getTitle(),
                        good.getDescription(),
                        good.getPrice(),
                        quantity
                )
        );
    }

    public static Item restore(UUID id, UUID goodId, String title, String description, double price, int quantity) {
        return new Item(
                id,
                goodId,
                title,
                description,
                price,
                quantity
        );
    }

    void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        this.quantity = quantity;
    }

    double getSubTotal() {
        return quantity * price;
    }
}
