package com.example.basket.infrastructure.adapters.out.persistence.good.entities;

import com.example.basket.infrastructure.adapters.out.persistence.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullUnmarked;

import java.util.UUID;

@Entity
@Table(name = "goods")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@NullUnmarked
public class GoodEntity extends BaseEntity {
    @Id
    private UUID id;
    private String title;
    private String description;
    private double price;
    private int weight;
    private int quantity;
}
