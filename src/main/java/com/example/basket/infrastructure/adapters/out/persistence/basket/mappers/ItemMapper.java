package com.example.basket.infrastructure.adapters.out.persistence.basket.mappers;

import org.springframework.stereotype.Component;
import com.example.basket.infrastructure.adapters.out.persistence.basket.entities.ItemEntity;
import com.example.basket.core.domain.model.basket.Item;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ItemMapper {
    public Set<ItemEntity> toJpaEntitiesCollection(List<Item> items) {
        return IntStream
                .range(0, items.size())
                .mapToObj(i -> this.toJpaEntity(items.get(i), i + 1))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<Item> toDomainEntitiesCollection(Collection<ItemEntity> itemEntities) {
        return itemEntities
                .stream()
                .map(this::toDomainEntity)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private ItemEntity toJpaEntity(Item item, int position) {
        return new ItemEntity(
                item.getId(),
                position,
                item.getGoodId(),
                item.getTitle(),
                item.getDescription(),
                item.getPrice(),
                item.getQuantity()
        );
    }

    private Item toDomainEntity(ItemEntity itemEntity) {
        return Item.restore(
                itemEntity.getId(),
                itemEntity.getGoodId(),
                itemEntity.getTitle(),
                itemEntity.getDescription(),
                itemEntity.getPrice(),
                itemEntity.getQuantity()
        );
    }
}
