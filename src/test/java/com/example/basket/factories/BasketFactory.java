package com.example.basket.factories;

import net.datafaker.Faker;
import com.example.basket.core.domain.kernel.Discount;
import com.example.basket.core.domain.model.basket.Basket;
import com.example.basket.core.domain.model.basket.DeliveryPeriod;

import java.util.UUID;

public class BasketFactory {
    private static final Faker faker = new Faker();

    private BasketFactory() {
    }

    public static Basket buildUnconfirmed() {
        return Basket.of(UUID.randomUUID());
    }

    public static Basket buildConfirmed() {
        var basket = buildReadyForCheckout();
        basket.checkout(Discount.of(faker.number().numberBetween(1, 99)));

        return basket;
    }

    public static Basket buildReadyForCheckout() {
        var basket = Basket.of(UUID.randomUUID());
        basket.change(GoodFactory.build(), faker.number().numberBetween(1, 100));
        basket.addAddress(AddressFactory.build());
        basket.addDeliveryPeriod(DeliveryPeriod.MIDDAY);

        return basket;
    }
}
