package com.example.basket.core.domain.services;

import org.springframework.stereotype.Service;
import com.example.basket.core.domain.kernel.Weight;
import com.example.basket.core.domain.model.basket.Basket;
import com.example.basket.core.domain.model.good.Good;

import java.util.List;
import java.util.UUID;

@Service
public class PromoGoodsService {
    public void addPromo(Basket basket) {
        var total = basket.getTotal();
        if (total > 5000) {
            basket.change(PromoGoods.SNACK, 1);
        } else if (total > 2000) {
            basket.change(PromoGoods.CANDY, 1);
        } else if (total > 1000) {
            basket.change(PromoGoods.GUM, 1);
        }
    }

    public static class PromoGoods {
        static final Good GUM = Good.of(
                UUID.fromString("5a2ea98c-d190-461f-9c0c-bf42330d10f6"),
                "Gum",
                "Promo",
                1,
                10,
                Weight.of(1)
        );

        static final Good CANDY = Good.of(
                UUID.fromString("01bc1a96-eabf-4881-9936-8a0654fefe20"),
                "Candy",
                "Promo",
                1,
                10,
                Weight.of(1)
        );

        static final Good SNACK = Good.of(
                UUID.fromString("0f7691e6-daa7-4615-b27e-9a70bcda5f5c"),
                "Snack",
                "Promo",
                1,
                10,
                Weight.of(1)
        );

        public static final List<Good> ALL = List.of(GUM, CANDY, SNACK);
    }
}
