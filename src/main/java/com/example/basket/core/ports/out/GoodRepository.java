package com.example.basket.core.ports.out;

import com.example.basket.core.domain.model.good.Good;

import java.util.List;
import java.util.UUID;

public interface GoodRepository {
    Good get(UUID id);

    List<Good> findAll();

    void create(Good good);

    void update(Good good);
}
