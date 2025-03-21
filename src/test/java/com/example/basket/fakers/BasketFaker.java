package com.example.basket.fakers;

import net.datafaker.Faker;
import com.example.basket.core.domain.kernel.Discount;
import com.example.basket.core.domain.model.basket.Basket;
import com.example.basket.core.domain.model.basket.DeliveryPeriod;

import java.util.UUID;

public class BasketFaker {
    private final Faker faker = new Faker();
    private final AddressFaker addressFaker = new AddressFaker();
    private final GoodFaker goodFaker = new GoodFaker();

    public Basket makeCreated() {
        return Basket.of(UUID.randomUUID());
    }

    public Basket makeConfirmed() {
        var basket = Basket.of(UUID.randomUUID());
        basket.change(goodFaker.make(), faker.number().numberBetween(1, 100));
        basket.addAddress(addressFaker.make());
        basket.addDeliveryPeriod(DeliveryPeriod.DAY);
        basket.checkout(Discount.of(faker.number().numberBetween(1, 99)));

        return basket;
    }
}
