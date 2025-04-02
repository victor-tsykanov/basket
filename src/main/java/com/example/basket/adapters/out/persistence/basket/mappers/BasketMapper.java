package com.example.basket.adapters.out.persistence.basket.mappers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import com.example.basket.adapters.out.persistence.basket.entities.BasketEntity;
import com.example.basket.core.domain.model.basket.Basket;


@Component
@AllArgsConstructor
public class BasketMapper {
    private ItemMapper itemMapper;
    private AddressMapper addressMapper;
    private DeliverPeriodMapper deliverPeriodMapper;

    public BasketEntity toJpaEntity(Basket basket) {
        return new BasketEntity(
                basket.getId(),
                basket.getBuyerId(),
                basket.getStatus().name(),
                itemMapper.toJpaEntitiesCollection(basket.getItems()),
                addressMapper.toJpaEntity(basket.getAddress()),
                deliverPeriodMapper.toJpaEntity(basket.getDeliveryPeriod()),
                basket.getTotal()
        );
    }

    public Basket toDomainEntity(BasketEntity basketEntity) {
        return Basket.restore(
                basketEntity.getId(),
                basketEntity.getBuyerId(),
                Basket.Status.valueOf(basketEntity.getStatus()),
                itemMapper.toDomainEntitiesCollection(basketEntity.getItems()),
                addressMapper.toDomainEntity(basketEntity.getAddress()),
                deliverPeriodMapper.toDomainEntity(basketEntity.getDeliveryPeriod()),
                basketEntity.getTotal()
        );
    }
}
