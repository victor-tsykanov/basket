package com.example.basket.adapters.out.persistence.basket.seeders;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.example.basket.adapters.out.persistence.basket.mappers.DeliverPeriodMapper;
import com.example.basket.adapters.out.persistence.basket.repositories.SpringDataDeliveryPeriodRepository;
import com.example.basket.core.domain.model.basket.DeliveryPeriod;

import java.util.Objects;

@Component
@AllArgsConstructor
public class SeedDeliveryPeriods implements ApplicationRunner {
    private final SpringDataDeliveryPeriodRepository repository;
    private final DeliverPeriodMapper mapper;

    @Override
    public void run(ApplicationArguments args) {
        repository.saveAll(
                DeliveryPeriod.LIST
                        .stream()
                        .map(mapper::toJpaEntity)
                        .filter(Objects::nonNull)
                        .toList()
        );
    }
}
