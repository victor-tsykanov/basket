package com.example.basket.infrastructure.adapters.out.persistence.good.seeders;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import com.example.basket.infrastructure.adapters.out.persistence.good.mappers.GoodMapper;
import com.example.basket.infrastructure.adapters.out.persistence.good.repositories.SpringDataGoodRepository;
import com.example.basket.core.domain.services.PromoGoodsService;

@Component
@AllArgsConstructor
public class SeedPromoGoods implements ApplicationRunner {
    private final SpringDataGoodRepository repository;
    private final GoodMapper mapper;

    @Override
    public void run(ApplicationArguments args) {
        repository.saveAll(
                PromoGoodsService.PromoGoods.ALL
                        .stream()
                        .map(mapper::toJpaEntity)
                        .toList()
        );
    }
}
