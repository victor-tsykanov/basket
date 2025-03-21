package com.example.basket.core.domain.model.good;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import com.example.basket.core.domain.kernel.Weight;

import java.util.UUID;

import static com.example.basket.common.Validation.requireValid;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class Good {
    @NotNull
    private final UUID id;

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @Min(0)
    private double price;

    @Min(0)
    private int quantity;

    @NotNull
    private Weight weight;

    public static Good of(UUID id, String title, String description, double price, int quantity, Weight weight) {
        return requireValid(
                new Good(id, title, description, price, quantity, weight)
        );
    }

    public static Good restore(UUID id, String title, String description, double price, int quantity, Weight weight) {
        return new Good(id, title, description, price, quantity, weight);
    }

    public void changeStocks(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        this.quantity = quantity;
    }
}
