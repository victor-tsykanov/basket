package com.example.basket.infrastructure.adapters.out.persistence.basket.entities;

import com.example.basket.infrastructure.adapters.out.persistence.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullUnmarked;

@Entity
@Table(name = "delivery_periods")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@NullUnmarked
public class DeliveryPeriodEntity extends BaseEntity {
    @Id
    private Integer id;
    private String name;
    private Integer from;
    private Integer to;
}
