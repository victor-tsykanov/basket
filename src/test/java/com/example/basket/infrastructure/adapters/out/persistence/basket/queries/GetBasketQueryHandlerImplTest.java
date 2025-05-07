package com.example.basket.infrastructure.adapters.out.persistence.basket.queries;

import com.example.basket.infrastructure.adapters.out.persistence.PersistenceTestsConfiguration;
import com.example.basket.common.exceptions.EntityNotFoundException;
import com.example.basket.core.ports.in.getbasket.GetBasketQuery;
import com.example.basket.core.ports.in.getbasket.GetBasketQueryHandler;
import com.example.basket.core.ports.in.getbasket.GetBasketQueryResponse.Item;
import com.example.basket.core.ports.out.BasketRepository;
import com.example.basket.factories.BasketFactory;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.transaction.TestTransaction;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest()
@Import(PersistenceTestsConfiguration.class)
class GetBasketQueryHandlerImplTest {
    @Autowired
    private GetBasketQueryHandler queryHandler;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private JdbcClient jdbcClient;

    @AfterEach
    void cleanUpBaskets() {
        jdbcClient
                .sql("truncate table baskets cascade")
                .update();
    }

    @Test
    void handle_should_returnBasket_when_basketExists() {
        // Arrange
        var basket = BasketFactory.buildReadyForCheckout();
        basketRepository.create(basket);
        TestTransaction.flagForCommit();
        TestTransaction.end();
        var command = GetBasketQuery.of(basket.getId());

        // Act
        var response = queryHandler.handle(command);

        // Assert
        var deliveryPeriod = Objects.requireNonNull(basket.getDeliveryPeriod());
        var address = response.address();
        var expectedAddress = Objects.requireNonNull(basket.getAddress());

        assertAll(
                () -> assertThat(response.id()).isEqualTo(basket.getId()),
                () -> assertThat(response.status()).isEqualTo(basket.getStatus().name()),
                () -> assertThat(response.deliveryPeriod()).isEqualTo(deliveryPeriod.getName()),
                () -> assertThat(response.items())
                        .extracting(Item::id, Item::goodId, Item::quantity)
                        .containsExactly(
                                basket
                                        .getItems()
                                        .stream()
                                        .map(item -> tuple(
                                                item.getId(),
                                                item.getGoodId(),
                                                item.getQuantity()
                                        ))
                                        .toArray(Tuple[]::new)
                        ),
                () -> assertThat(address.country()).isEqualTo(expectedAddress.getCountry()),
                () -> assertThat(address.city()).isEqualTo(expectedAddress.getCity()),
                () -> assertThat(address.street()).isEqualTo(expectedAddress.getStreet()),
                () -> assertThat(address.house()).isEqualTo(expectedAddress.getHouse()),
                () -> assertThat(address.apartment()).isEqualTo(expectedAddress.getApartment())
        );
    }

    @Test
    void handle_should_throwException_when_basketDoesNotExist() {
        // Arrange
        var command = GetBasketQuery.of(UUID.randomUUID());

        // Act
        assertThrows(
                EntityNotFoundException.class,
                () -> queryHandler.handle(command)
        );
    }
}
