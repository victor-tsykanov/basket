package com.example.basket.fakers;

import net.datafaker.Faker;
import com.example.basket.core.domain.kernel.Address;

public class AddressFaker extends Faker {
    private static final net.datafaker.providers.base.Address addressFaker = new Faker().address();

    public Address make() {
        return Address.of(
                addressFaker.country(),
                addressFaker.city(),
                addressFaker.streetName(),
                addressFaker.buildingNumber(),
                addressFaker.secondaryAddress()
        );
    }
}
