package com.example.basket.core.ports.out;

import com.example.basket.core.domain.model.basket.Basket;

import java.util.UUID;

public interface BasketRepository {
    Basket get(UUID id);

    void create(Basket basket);

    void update(Basket basket);
}
