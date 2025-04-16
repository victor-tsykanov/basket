package com.example.basket.core.ports.out;

import com.example.basket.core.domain.kernel.Discount;
import com.example.basket.core.domain.model.basket.Basket;

public interface DiscountClient {
    Discount getDiscount(Basket basket);
}
