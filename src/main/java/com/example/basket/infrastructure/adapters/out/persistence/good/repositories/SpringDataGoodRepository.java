package com.example.basket.infrastructure.adapters.out.persistence.good.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.basket.infrastructure.adapters.out.persistence.good.entities.GoodEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataGoodRepository extends CrudRepository<GoodEntity, UUID> {
    @Override
    List<GoodEntity> findAll();
}
