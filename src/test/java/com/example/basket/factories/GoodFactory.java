package com.example.basket.factories;

import net.datafaker.Faker;
import com.example.basket.core.domain.kernel.Weight;
import com.example.basket.core.domain.model.good.Good;

import java.util.UUID;

public class GoodFactory {
    private static final Faker faker = new Faker();

    private GoodFactory() {
    }

    public static Good build() {
        return Good.of(
                UUID.randomUUID(),
                faker.word().noun(),
                faker.word().adjective(),
                faker.number().randomDouble(2, 0, 10),
                faker.number().positive(),
                Weight.of(faker.number().positive())
        );
    }
}
