package com.example.basket.core.domain.services;

import org.junit.jupiter.api.Test;
import com.example.basket.core.domain.kernel.Weight;
import com.example.basket.core.domain.model.basket.Basket;
import com.example.basket.core.domain.model.basket.Item;
import com.example.basket.core.domain.model.good.Good;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class PromoGoodsServiceTest {

    @Test
    void addPromo_should_addCandy_when_totalIsBetween2000And5000() {
        var promoGoodsService = new PromoGoodsService();
        var basket = Basket.of(UUID.randomUUID());
        var book = Good.of(
                UUID.randomUUID(),
                "Book",
                "...",
                1050,
                2,
                Weight.of(1)
        );
        basket.change(book, 2);

        promoGoodsService.addPromo(basket);

        assertThat(basket.getItems())
                .extracting(Item::getGoodId, Item::getQuantity)
                .containsExactly(
                        tuple(book.getId(), 2),
                        tuple(PromoGoodsService.PromoGoods.CANDY.getId(), 1)
                );
    }

    @Test
    void addPromo_should_doNothing_when_totalIsLessThan1000() {
        var promoGoodsService = new PromoGoodsService();
        var basket = Basket.of(UUID.randomUUID());
        var book = Good.of(
                UUID.randomUUID(),
                "Book",
                "...",
                50,
                2,
                Weight.of(1)
        );
        basket.change(book, 2);

        promoGoodsService.addPromo(basket);

        assertThat(basket.getItems())
                .extracting(Item::getGoodId, Item::getQuantity)
                .containsExactly(
                        tuple(book.getId(), 2)
                );
    }
}
