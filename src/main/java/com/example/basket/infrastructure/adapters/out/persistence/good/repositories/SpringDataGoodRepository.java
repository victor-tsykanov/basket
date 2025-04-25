package com.example.basket.infrastructure.adapters.out.persistence.good.repositories;

import com.example.basket.infrastructure.adapters.out.persistence.good.entities.GoodEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SpringDataGoodRepository extends CrudRepository<GoodEntity, UUID> {
    List<GoodEntity> findAllByOrderByCreatedAtAsc();
}
