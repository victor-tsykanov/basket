package com.example.basket.core.domain.model.good;

import jakarta.validation.ConstraintViolationException;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import com.example.basket.core.domain.kernel.Weight;
import com.example.basket.fakers.GoodFaker;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GoodTest {
    private final Faker faker = new Faker();
    private final GoodFaker goodFaker = new GoodFaker();

    @Test
    void of_should_createGood_when_allParametersAreValid() {
        var id = UUID.randomUUID();
        var title = faker.word().noun();
        var description = faker.word().adjective();
        var price = faker.number().randomDouble(2, 0, 10);
        var quantity = faker.number().positive();
        var weight = Weight.of(faker.number().positive());

        var good = Good.of(id, title, description, price, quantity, weight);

        assertThat(good.getId()).isEqualTo(id);
        assertThat(good.getTitle()).isEqualTo(title);
        assertThat(good.getDescription()).isEqualTo(description);
        assertThat(good.getPrice()).isEqualTo(price);
        assertThat(good.getQuantity()).isEqualTo(quantity);
        assertThat(good.getWeight()).isEqualTo(weight);
    }

    @Test
    void of_should_throwException_when_parametersAreNotValid() {
        assertThrows(
                ConstraintViolationException.class,
                () -> Good.of(
                        UUID.randomUUID(),
                        "",
                        "",
                        0,
                        0,
                        Weight.of(1)
                )
        );
    }

    @Test
    void changeStocks_should_changeQuantity_when_quantityIsNonNegative() {
        var good = goodFaker.make();
        var newQuantity = good.getQuantity() + 5;

        good.changeStocks(newQuantity);

        assertThat(good.getQuantity()).isEqualTo(newQuantity);
    }

    @Test
    void changeStocks_should_throwException_when_quantityIsNegative() {
        var good = goodFaker.make();

        assertThrows(
                IllegalArgumentException.class,
                () -> good.changeStocks(-1)
        );
    }
}
