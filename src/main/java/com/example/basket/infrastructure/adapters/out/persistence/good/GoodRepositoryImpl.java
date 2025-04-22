package com.example.basket.infrastructure.adapters.out.persistence.good;

import com.example.basket.infrastructure.adapters.out.persistence.good.mappers.GoodMapper;
import com.example.basket.infrastructure.adapters.out.persistence.good.repositories.SpringDataGoodRepository;
import com.example.basket.common.exceptions.EntityExistsException;
import com.example.basket.common.exceptions.EntityNotFoundException;
import com.example.basket.core.domain.model.good.Good;
import com.example.basket.core.ports.out.GoodRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GoodRepositoryImpl implements GoodRepository {
    private final SpringDataGoodRepository springDataGoodRepository;
    private final GoodMapper goodMapper;

    @Override
    @Transactional
    public Good get(UUID id) {
        return springDataGoodRepository
                .findById(id)
                .map(goodMapper::toDomainEntity)
                .orElseThrow(() -> new EntityNotFoundException("Good with id=%s is not found".formatted(id)));
    }

    @Override
    public List<Good> findAll() {
        return springDataGoodRepository
                .findAll()
                .stream()
                .map(goodMapper::toDomainEntity)
                .toList();
    }

    @Override
    @Transactional
    public void create(Good good) {
        if (springDataGoodRepository.existsById(good.getId())) {
            throw new EntityExistsException("Good with id=%s already exists".formatted(good.getId()));
        }

        var goodEntity = goodMapper.toJpaEntity(good);
        springDataGoodRepository.save(goodEntity);
    }

    @Override
    @Transactional
    public void update(Good good) {
        if (!springDataGoodRepository.existsById(good.getId())) {
            throw new EntityNotFoundException("Good with id=%s is not found".formatted(good.getId()));
        }

        var goodEntity = goodMapper.toJpaEntity(good);
        springDataGoodRepository.save(goodEntity);
    }
}
