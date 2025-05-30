package com.example.basket.core.domain.model.basket;

import jakarta.validation.ConstraintViolationException;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import com.example.basket.factories.GoodFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemTest {
    private final Faker faker = new Faker();

    @Test
    void of_should_createItem_when_allParametersAreValid() {
        var good = GoodFactory.build();
        var quantity = faker.number().positive();

        var item = Item.of(good, quantity);

        assertAll(
                () -> assertThat(item.getId()).isNotNull(),
                () -> assertThat(item.getGoodId()).isEqualTo(good.getId()),
                () -> assertThat(item.getTitle()).isEqualTo(good.getTitle()),
                () -> assertThat(item.getDescription()).isEqualTo(good.getDescription()),
                () -> assertThat(item.getPrice()).isEqualTo(good.getPrice()),
                () -> assertThat(item.getQuantity()).isEqualTo(quantity)
        );
    }

    @Test
    void of_should_throwException_when_quantityIsNegative() {
        var good = GoodFactory.build();

        assertThrows(
                ConstraintViolationException.class,
                () -> Item.of(good, -1)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 15})
    void setQuantity_should_updateQuantity_when_newValueIsValid(int newQuantity) {
        var good = GoodFactory.build();
        var item = Item.of(good, 19);

        item.setQuantity(newQuantity);

        assertThat(item.getQuantity()).isEqualTo(newQuantity);
    }

    @Test
    void setQuantity_should_throwException_when_newValueIsNegative() {
        var good = GoodFactory.build();
        var item = Item.of(good, 19);

        assertThrows(
                IllegalArgumentException.class,
                () -> item.setQuantity(-1)
        );
    }

    @Test
    void getSubTotal_should_itemTotalPrice() {
        var good = GoodFactory.build();
        var item = Item.of(good, 5);

        var subTotal = item.getSubTotal();

        assertThat(subTotal).isEqualTo(5 * good.getPrice());
    }
}
