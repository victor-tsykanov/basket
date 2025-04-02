package com.example.basket.adapters.out.persistence.basket.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NullUnmarked;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "baskets")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@NullUnmarked
public class BasketEntity {
    @Id
    private UUID id;
    private UUID buyerId;
    private String status;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "basket_id", nullable = false)
    @OrderBy("position")
    private Set<ItemEntity> items;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name="country", column = @Column(name = "address_country")),
            @AttributeOverride(name="street", column = @Column(name = "address_street")),
            @AttributeOverride(name="house", column = @Column(name = "address_house")),
            @AttributeOverride(name="apartment", column = @Column(name = "address_apartment"))
    })
    private AddressEntity address;

    @OneToOne
    private DeliveryPeriodEntity deliveryPeriod;

    private Double total;
}
