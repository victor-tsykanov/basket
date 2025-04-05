package com.example.basket.core.domain.kernel;

import jakarta.validation.ConstraintViolationException;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddressTest {
    private static final net.datafaker.providers.base.Address faker = new Faker().address();

    @Test
    void of_should_createAddress_when_allAddressPartsAreValid() {
        var country = faker.country();
        var city = faker.city();
        var street = faker.streetName();
        var house = faker.buildingNumber();
        var apartment = faker.secondaryAddress();

        var address = Address.of(
                country,
                city,
                street,
                house,
                apartment
        );

        assertThat(address.getCountry()).isEqualTo(country);
        assertThat(address.getCity()).isEqualTo(city);
        assertThat(address.getStreet()).isEqualTo(street);
        assertThat(address.getHouse()).isEqualTo(house);
        assertThat(address.getApartment()).isEqualTo(apartment);
    }

    @Test
    void of_should_throwException_when_addressPartsAreNotValid() {
        assertThrows(
                ConstraintViolationException.class,
                () -> Address.of(
                        faker.country(),
                        faker.city(),
                        faker.streetName(),
                        "",
                        faker.secondaryAddress()
                )
        );
    }
}
