package com.example.basket.adapters.out.persistence.basket.queries;

import com.example.basket.adapters.out.persistence.PersistenceTestsConfiguration;
import com.example.basket.common.exceptions.EntityNotFoundException;
import com.example.basket.core.ports.in.getbasket.GetBasketQuery;
import com.example.basket.core.ports.in.getbasket.GetBasketQueryHandler;
import com.example.basket.core.ports.out.BasketRepository;
import com.example.basket.fakers.BasketFaker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.transaction.TestTransaction;

import java.util.Objects;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThrows;

@DataJpaTest()
@Import(PersistenceTestsConfiguration.class)
class GetBasketQueryHandlerImplTest {
    @Autowired
    private GetBasketQueryHandler queryHandler;

    @Autowired
    private BasketRepository basketRepository;

    private static final BasketFaker basketFaker = new BasketFaker();

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
        var basket = basketFaker.makeReadyForCheckout();
        basketRepository.create(basket);
        TestTransaction.flagForCommit();
        TestTransaction.end();
        var command = GetBasketQuery.of(basket.getId());

        // Act
        var response = queryHandler.handle(command);

        // Assert
        assertThat(response.id()).isEqualTo(basket.getId());
        assertThat(response.status()).isEqualTo(basket.getStatus().name());

        var address = response.address();
        var expectedAddress = Objects.requireNonNull(basket.getAddress());

        assertThat(address.country()).isEqualTo(expectedAddress.getCountry());
        assertThat(address.street()).isEqualTo(expectedAddress.getStreet());
        assertThat(address.house()).isEqualTo(expectedAddress.getHouse());
        assertThat(address.apartment()).isEqualTo(expectedAddress.getApartment());
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
