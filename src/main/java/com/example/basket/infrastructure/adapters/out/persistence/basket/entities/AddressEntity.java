package com.example.basket.infrastructure.adapters.out.persistence.basket.entities;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullUnmarked;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@NullUnmarked
public class AddressEntity {
    private String country;
    private String city;
    private String street;
    private String house;
    private String apartment;
}
