package com.example.basket.infrastructure.adapters.out.persistence.basket;

import com.example.basket.infrastructure.adapters.out.persistence.basket.mappers.BasketMapper;
import com.example.basket.infrastructure.adapters.out.persistence.basket.repositories.SpringDataBasketRepository;
import com.example.basket.common.exceptions.EntityExistsException;
import com.example.basket.common.exceptions.EntityNotFoundException;
import com.example.basket.core.domain.model.basket.Basket;
import com.example.basket.core.ports.out.BasketRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class BasketRepositoryImpl implements BasketRepository {
    private final SpringDataBasketRepository springDataBasketRepository;
    private final BasketMapper basketMapper;

    @Override
    @Transactional
    public Basket get(UUID id) {
        return springDataBasketRepository
                .findById(id)
                .map(basketMapper::toDomainEntity)
                .orElseThrow(() -> new EntityNotFoundException("Basket with id=%s is not found".formatted(id)));
    }

    @Override
    @Transactional
    public boolean exists(UUID id) {
        return springDataBasketRepository.existsById(id);
    }

    @Override
    @Transactional
    public void create(Basket basket) {
        if (springDataBasketRepository.existsById(basket.getId())) {
            throw new EntityExistsException("Basket with id=%s already exists".formatted(basket.getId()));
        }

        var basketEntity = basketMapper.toJpaEntity(basket);
        springDataBasketRepository.save(basketEntity);
    }

    @Override
    @Transactional
    public void update(Basket basket) {
        if (!springDataBasketRepository.existsById(basket.getId())) {
            throw new EntityNotFoundException("Basket with id=%s is not found".formatted(basket.getId()));
        }

        var basketEntity = basketMapper.toJpaEntity(basket);
        springDataBasketRepository.save(basketEntity);
    }
}
