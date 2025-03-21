package com.example.basket.core.domain.model.basket;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import com.example.basket.core.domain.kernel.Address;
import com.example.basket.core.domain.kernel.Discount;
import com.example.basket.core.domain.model.good.Good;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.basket.common.Validation.requireValid;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Basket {
    @NotNull
    private final UUID id;

    @NotNull
    private final UUID buyerId;

    @NotNull
    private Status status;

    @NotNull
    private List<Item> items;

    @Nullable
    private Address address;

    @Nullable
    private DeliveryPeriod deliveryPeriod;

    private double total;

    public static Basket of(UUID buyerId) {
        return requireValid(
                new Basket(
                        UUID.randomUUID(),
                        buyerId,
                        Status.CREATED,
                        new ArrayList<>(),
                        null,
                        null,
                        0
                )
        );
    }

    public static Basket restore(
            UUID id,
            UUID buyerId,
            Status status,
            List<Item> items,
            @Nullable Address address,
            @Nullable DeliveryPeriod deliveryPeriod,
            double total
    ) {
        return new Basket(
                id,
                buyerId,
                status,
                items,
                address,
                deliveryPeriod,
                total
        );
    }

    public void change(Good good, int quantity) {
        if (quantity == 0) {
            items.removeIf(i -> i.getGoodId().equals(good.getId()));
        } else {
            items
                    .stream()
                    .filter(i -> i.getGoodId().equals(good.getId()))
                    .findFirst()
                    .ifPresentOrElse(
                            item -> item.setQuantity(quantity),
                            () -> items.add(Item.of(good, quantity))
                    );
        }

        total = items
                .stream()
                .mapToDouble(Item::getSubTotal)
                .sum();
    }

    public void addAddress(Address address) {
        if (status == Status.CONFIRMED) {
            throw new IllegalStateException("Basket is already confirmed");
        }

        this.address = address;
    }

    public void addDeliveryPeriod(DeliveryPeriod deliveryPeriod) {
        if (status == Status.CONFIRMED) {
            throw new IllegalStateException("Basket is already confirmed");
        }

        this.deliveryPeriod = deliveryPeriod;
    }

    public void checkout(Discount discount) {
        if (status == Status.CONFIRMED) {
            throw new IllegalStateException("Basket is already confirmed");
        }

        if (items.isEmpty()) {
            throw new IllegalStateException("Basket is empty");
        }

        if (address == null) {
            throw new IllegalStateException("Address is not set");
        }

        if (deliveryPeriod == null) {
            throw new IllegalStateException("Delivery period is not set");
        }

        total = discount.apply(total);
        status = Status.CONFIRMED;
    }

    public enum Status {
        CREATED, CONFIRMED
    }
}
