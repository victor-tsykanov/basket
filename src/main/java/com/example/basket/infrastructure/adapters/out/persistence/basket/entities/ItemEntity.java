package com.example.basket.infrastructure.adapters.out.persistence.basket.entities;

import com.example.basket.infrastructure.adapters.out.persistence.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullUnmarked;

import java.util.UUID;

@Entity
@Table(name = "items")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@NullUnmarked
@EqualsAndHashCode(of = "id", callSuper = false)
public class ItemEntity extends BaseEntity {
    @Id
    private UUID id;
    private Integer position;
    private UUID goodId;
    private String title;
    private String description;
    private Double price;
    private Integer quantity;
}
