package com.example.basket.infrastructure.adapters.out.persistence.basket.mappers;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;
import com.example.basket.infrastructure.adapters.out.persistence.basket.entities.DeliveryPeriodEntity;
import com.example.basket.core.domain.model.basket.DeliveryPeriod;

@Component
public class DeliverPeriodMapper {
    public @Nullable DeliveryPeriodEntity toJpaEntity(@Nullable DeliveryPeriod deliveryPeriod) {
        if (deliveryPeriod == null) {
            return null;
        }

        return new DeliveryPeriodEntity(
                deliveryPeriod.getId(),
                deliveryPeriod.getName(),
                deliveryPeriod.getFrom(),
                deliveryPeriod.getTo()
        );
    }

    public @Nullable DeliveryPeriod toDomainEntity(@Nullable DeliveryPeriodEntity deliveryPeriodEntity) {
        if (deliveryPeriodEntity == null) {
            return null;
        }

        return DeliveryPeriod.restore(
                deliveryPeriodEntity.getId(),
                deliveryPeriodEntity.getName(),
                deliveryPeriodEntity.getFrom(),
                deliveryPeriodEntity.getTo()
        );
    }
}
