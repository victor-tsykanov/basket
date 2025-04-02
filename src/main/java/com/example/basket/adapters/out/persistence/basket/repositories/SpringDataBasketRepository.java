package com.example.basket.adapters.out.persistence.basket.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.basket.adapters.out.persistence.basket.entities.BasketEntity;

import java.util.UUID;

@Repository
public interface SpringDataBasketRepository extends CrudRepository<BasketEntity, UUID> {
}
