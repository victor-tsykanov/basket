package com.example.basket.infrastructure.adapters.out.persistence.basket.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.example.basket.infrastructure.adapters.out.persistence.basket.entities.DeliveryPeriodEntity;

@Repository
public interface SpringDataDeliveryPeriodRepository extends CrudRepository<DeliveryPeriodEntity, Integer> {
}
