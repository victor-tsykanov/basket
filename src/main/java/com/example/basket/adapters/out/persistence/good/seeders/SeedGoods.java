package com.example.basket.adapters.out.persistence.good.seeders;

import com.example.basket.adapters.out.persistence.good.mappers.GoodMapper;
import com.example.basket.adapters.out.persistence.good.repositories.SpringDataGoodRepository;
import com.example.basket.core.domain.kernel.Weight;
import com.example.basket.core.domain.model.good.Good;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Stream;

@Component
@AllArgsConstructor
public class SeedGoods implements ApplicationRunner {
    private final SpringDataGoodRepository repository;
    private final GoodMapper mapper;

    @Override
    public void run(ApplicationArguments args) {
        repository.saveAll(
                Stream.of(
                                Good.of(
                                        UUID.fromString("539eed47-d75b-4c46-9105-c87bdf525839"),
                                        "Bread",
                                        "...",
                                        100,
                                        10,
                                        Weight.of(1)
                                ),
                                Good.of(
                                        UUID.fromString("e8cb8a0b-d302-485a-801c-5fb50aced4d5"),
                                        "Milk",
                                        "Milk description",
                                        200,
                                        10,
                                        Weight.of(9)
                                ),
                                Good.of(
                                        UUID.fromString("a1d48be9-4c98-4371-97c0-064bde03874e"),
                                        "Eggs",
                                        "Eggs description",
                                        300,
                                        10,
                                        Weight.of(8)
                                ),
                                Good.of(
                                        UUID.fromString("34b1e64a-6471-44a0-8c4a-e5d21584a76c"),
                                        "Sausage",
                                        "Sausage description",
                                        400,
                                        10,
                                        Weight.of(4)
                                ),
                                Good.of(
                                        UUID.fromString("292dc3c5-2bdd-4e0c-bd75-c5e8b07a8792"),
                                        "Coffee",
                                        "Coffee description",
                                        500,
                                        10,
                                        Weight.of(7)
                                ),
                                Good.of(
                                        UUID.fromString("a3fcc8e1-d2a3-4bd6-9421-c82019e21c2d"),
                                        "Sugar",
                                        "Sugar description",
                                        600,
                                        10,
                                        Weight.of(1)
                                )
                        )
                        .map(mapper::toJpaEntity)
                        .toList()
        );
    }
}
