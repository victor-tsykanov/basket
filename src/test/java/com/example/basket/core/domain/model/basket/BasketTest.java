package com.example.basket.core.domain.model.basket;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import com.example.basket.core.domain.kernel.Discount;
import com.example.basket.core.domain.kernel.Weight;
import com.example.basket.core.domain.model.good.Good;
import com.example.basket.factories.AddressFactory;
import com.example.basket.factories.BasketFactory;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BasketTest {
    private static final Good apple = Good.of(
            UUID.randomUUID(),
            "apple",
            "...",
            1,
            100,
            Weight.of(1)
    );
    private static final Good orange = Good.of(
            UUID.randomUUID(),
            "orange",
            "...",
            10,
            100,
            Weight.of(1)
    );

    @Test
    void of_should_createBasket_when_allParametersAreValid() {
        var buyerId = UUID.randomUUID();

        var basket = Basket.of(buyerId);

        assertAll(
                () -> assertThat(basket.getId()).isNotNull(),
                () -> assertThat(basket.getBuyerId()).isEqualTo(buyerId),
                () -> assertThat(basket.getStatus()).isEqualTo(Basket.Status.CREATED)
        );
    }

    @Test
    void change_should_addItem_when_goodIsNotInBasketAndQuantityIsPositive() {
        var basket = Basket.of(UUID.randomUUID());

        basket.change(apple, 1);
        basket.change(orange, 2);

        assertAll(
                () -> assertThat(basket.getItems())
                        .extracting(Item::getGoodId, Item::getQuantity)
                        .containsExactly(
                                tuple(apple.getId(), 1),
                                tuple(orange.getId(), 2)
                        ),
                () -> assertThat(basket.getTotal()).isEqualTo(apple.getPrice() + orange.getPrice() * 2)
        );
    }

    @Test
    void change_should_doNothing_when_goodIsNotInBasketAndQuantityIsZero() {
        var basket = Basket.of(UUID.randomUUID());
        basket.change(apple, 1);

        basket.change(orange, 0);

        assertAll(
                () -> assertThat(basket.getItems())
                        .extracting(Item::getGoodId, Item::getQuantity)
                        .containsExactly(
                                tuple(apple.getId(), 1)
                        ),
                () -> assertThat(basket.getTotal()).isEqualTo(apple.getPrice())
        );
    }

    @Test
    void change_should_updateQuantity_when_goodIsInBasketAndQuantityIsPositive() {
        var basket = Basket.of(UUID.randomUUID());
        basket.change(apple, 1);
        basket.change(orange, 2);

        basket.change(orange, 3);

        assertAll(
                () -> assertThat(basket.getItems())
                        .extracting(Item::getGoodId, Item::getQuantity)
                        .containsExactly(
                                tuple(apple.getId(), 1),
                                tuple(orange.getId(), 3)
                        ),
                () -> assertThat(basket.getTotal()).isEqualTo(apple.getPrice() + orange.getPrice() * 3)
        );
    }

    @Test
    void change_should_removeItem_when_goodIsInBasketAndQuantityIsZero() {
        var basket = Basket.of(UUID.randomUUID());
        basket.change(apple, 1);
        basket.change(orange, 2);

        basket.change(apple, 0);

        assertAll(
                () -> assertThat(basket.getItems())
                        .extracting(Item::getGoodId, Item::getQuantity)
                        .containsExactly(
                                tuple(orange.getId(), 2)
                        ),
                () -> assertThat(basket.getTotal()).isEqualTo(orange.getPrice() * 2)
        );
    }

    @Test
    void change_should_throwException_when_quantityIsNegative() {
        var basket = Basket.of(UUID.randomUUID());

        assertThrows(
                ConstraintViolationException.class,
                () -> basket.change(apple, -1)
        );
    }

    @Test
    void addAddress_should_setAddress_when_statusIsCreated() {
        var basket = Basket.of(UUID.randomUUID());
        var address = AddressFactory.build();

        basket.addAddress(address);

        assertThat(basket.getAddress()).isEqualTo(address);
    }

    @Test
    void addAddress_should_throwException_when_statusIsConfirmed() {
        var basket = BasketFactory.buildConfirmed();
        var address = AddressFactory.build();

        assertThrows(
                IllegalStateException.class,
                () -> basket.addAddress(address)
        );
    }

    @Test
    void addDeliveryPeriod_should_setDeliveryPeriod_when_statusIsCreated() {
        var basket = Basket.of(UUID.randomUUID());

        basket.addDeliveryPeriod(DeliveryPeriod.MIDDAY);

        assertThat(basket.getDeliveryPeriod()).isEqualTo(DeliveryPeriod.MIDDAY);
    }

    @Test
    void addDeliveryPeriod_should_throwException_when_statusIsConfirmed() {
        var basket = BasketFactory.buildConfirmed();

        assertThrows(
                IllegalStateException.class,
                () -> basket.addDeliveryPeriod(DeliveryPeriod.MIDDAY)
        );
    }

    @Test
    void checkout_should_confirmBasketAndApplyDiscount() {
        var basket = Basket.of(UUID.randomUUID());
        basket.change(apple, 2);
        basket.addAddress(AddressFactory.build());
        basket.addDeliveryPeriod(DeliveryPeriod.EVENING);
        var discount = Discount.of(10);

        basket.checkout(discount);

        assertAll(
                () -> assertThat(basket.getStatus()).isEqualTo(Basket.Status.CONFIRMED),
                () -> assertThat(basket.getTotal()).isEqualTo(apple.getPrice() * 2 * 0.9)
        );
    }

    @Test
    void checkout_should_throwException_when_statusIsConfirmed() {
        var basket = BasketFactory.buildConfirmed();
        assertThrows(
                IllegalStateException.class,
                () -> basket.checkout(Discount.of(10))
        );
    }

    @Test
    void checkout_should_throwException_when_thereIsNoItems() {
        var basket = Basket.of(UUID.randomUUID());
        basket.addAddress(AddressFactory.build());
        basket.addDeliveryPeriod(DeliveryPeriod.MIDDAY);

        assertThrows(
                IllegalStateException.class,
                () -> basket.checkout(Discount.of(10))
        );
    }

    @Test
    void checkout_should_throwException_when_addressIsNotSet() {
        var basket = Basket.of(UUID.randomUUID());
        basket.change(apple, 1);
        basket.addDeliveryPeriod(DeliveryPeriod.MIDDAY);

        assertThrows(
                IllegalStateException.class,
                () -> basket.checkout(Discount.of(10))
        );
    }

    @Test
    void checkout_should_throwException_when_deliveryPeriodIsNotSet() {
        var basket = Basket.of(UUID.randomUUID());
        basket.change(apple, 1);
        basket.addAddress(AddressFactory.build());

        assertThrows(
                IllegalStateException.class,
                () -> basket.checkout(Discount.of(10))
        );
    }
}
