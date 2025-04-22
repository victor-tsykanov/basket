package com.example.basket.infrastructure.adapters.out.persistence.good.mappers;

import org.springframework.stereotype.Component;
import com.example.basket.infrastructure.adapters.out.persistence.good.entities.GoodEntity;
import com.example.basket.core.domain.kernel.Weight;
import com.example.basket.core.domain.model.good.Good;

@Component
public class GoodMapper {
    public GoodEntity toJpaEntity(Good good) {
        return new GoodEntity(
                good.getId(),
                good.getTitle(),
                good.getDescription(),
                good.getPrice(),
                good.getWeight().getValue(),
                good.getQuantity()
        );
    }

    public Good toDomainEntity(GoodEntity goodEntity) {
        return Good.restore(
                goodEntity.getId(),
                goodEntity.getTitle(),
                goodEntity.getDescription(),
                goodEntity.getPrice(),
                goodEntity.getQuantity(),
                Weight.of(goodEntity.getWeight())
        );
    }
}
