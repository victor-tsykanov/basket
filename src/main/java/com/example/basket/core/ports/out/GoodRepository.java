package com.example.basket.core.ports.out;

import com.example.basket.core.domain.model.good.Good;

import java.util.UUID;

public interface GoodRepository {
    Good get(UUID id);

    void create(Good good);

    void update(Good good);
}
