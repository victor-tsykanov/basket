package com.example.basket.adapters.out.persistence.basket.mappers;

import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;
import com.example.basket.adapters.out.persistence.basket.entities.AddressEntity;
import com.example.basket.core.domain.kernel.Address;

@Component
public class AddressMapper {
    public @Nullable AddressEntity toJpaEntity(@Nullable Address address) {
        if (address == null) {
            return null;
        }

        return new AddressEntity(
                address.getCountry(),
                address.getStreet(),
                address.getHouse(),
                address.getApartment()
        );
    }

    public @Nullable Address toDomainEntity(@Nullable AddressEntity addressEntity) {
        if (addressEntity == null) {
            return null;
        }

        return Address.of(
                addressEntity.getCountry(),
                addressEntity.getStreet(),
                addressEntity.getHouse(),
                addressEntity.getApartment()
        );
    }
}
