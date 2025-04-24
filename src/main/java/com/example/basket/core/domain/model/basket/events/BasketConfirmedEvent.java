package com.example.basket.core.domain.model.basket.events;

import com.example.basket.common.ddd.DomainEvent;
import com.example.basket.core.domain.model.basket.Basket;
import lombok.Getter;

import java.util.UUID;

@Getter
public final class BasketConfirmedEvent extends DomainEvent {
    private static final String type = "basket.confirmed.event";
    private final Basket basket;

    public BasketConfirmedEvent(Basket basket) {
        super(basket, UUID.randomUUID(), type);
        this.basket = basket;
    }
}
