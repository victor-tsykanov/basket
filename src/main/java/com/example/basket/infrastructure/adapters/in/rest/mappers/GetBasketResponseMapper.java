package com.example.basket.infrastructure.adapters.in.rest.mappers;

import com.example.basket.core.ports.in.getbasket.GetBasketQueryResponse;
import com.example.basket.infrastructure.adapters.in.rest.dto.Address;
import com.example.basket.infrastructure.adapters.in.rest.dto.Basket;
import com.example.basket.infrastructure.adapters.in.rest.dto.DeliveryPeriod;
import com.example.basket.infrastructure.adapters.in.rest.dto.ExistedItem;
import org.springframework.stereotype.Component;

@Component
public class GetBasketResponseMapper {
    public Basket toDto(GetBasketQueryResponse basket) {
        var addressDto = new Address()
                .country(basket.address().country())
                .city(basket.address().city())
                .street(basket.address().street())
                .house(basket.address().house())
                .apartment(basket.address().apartment());

        var itemsDto = basket
                .items()
                .stream()
                .map(item -> {
                    return new ExistedItem()
                            .id(item.id())
                            .goodId(item.goodId())
                            .quantity(item.quantity());
                })
                .toList();

        var status = switch (basket.status()) {
            case "CREATED" -> Basket.StatusEnum.CREATED;
            case "CONFIRMED" -> Basket.StatusEnum.CONFIRMED;
            default -> throw new RuntimeException("Unexpected value: " + basket.status());
        };

        return new Basket()
                .id(basket.id())
                .address(addressDto)
                .deliveryPeriod(DeliveryPeriod.fromValue(basket.deliveryPeriod()))
                .status(status)
                .items(itemsDto);
    }
}
