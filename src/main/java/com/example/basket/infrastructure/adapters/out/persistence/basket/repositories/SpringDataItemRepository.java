package com.example.basket.infrastructure.adapters.out.persistence.basket.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.basket.infrastructure.adapters.out.persistence.basket.entities.ItemEntity;

import java.util.UUID;

@Repository
public interface SpringDataItemRepository extends CrudRepository<ItemEntity, UUID> {
}
