package com.example.basket.factories;

import net.datafaker.Faker;
import com.example.basket.core.domain.kernel.Address;

public class AddressFactory {
    private static final net.datafaker.providers.base.Address addressFaker = new Faker().address();

    private AddressFactory() {
    }

    public static Address build() {
        return Address.of(
                addressFaker.country(),
                addressFaker.city(),
                addressFaker.streetName(),
                addressFaker.buildingNumber(),
                addressFaker.secondaryAddress()
        );
    }
}
