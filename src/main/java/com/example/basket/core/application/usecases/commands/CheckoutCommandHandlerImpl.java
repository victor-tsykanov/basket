package com.example.basket.core.application.usecases.commands;

import com.example.basket.core.domain.kernel.Discount;
import com.example.basket.core.domain.services.PromoGoodsService;
import com.example.basket.core.ports.in.chechout.CheckoutCommand;
import com.example.basket.core.ports.in.chechout.CheckoutCommandHandler;
import com.example.basket.core.ports.out.BasketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckoutCommandHandlerImpl implements CheckoutCommandHandler {
    private final BasketRepository basketRepository;
    private final PromoGoodsService promoGoodsService;

    @Override
    public void handle(CheckoutCommand command) {
        var basket = basketRepository.get(command.getBasketId());
        var discount = Discount.of(5);
        promoGoodsService.addPromo(basket);
        basket.checkout(discount);
        basketRepository.update(basket);
    }
}
