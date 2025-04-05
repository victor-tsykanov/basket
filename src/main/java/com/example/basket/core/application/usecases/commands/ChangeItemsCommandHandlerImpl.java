package com.example.basket.core.application.usecases.commands;

import com.example.basket.core.domain.model.basket.Basket;
import com.example.basket.core.ports.in.changeitems.ChangeItemsCommand;
import com.example.basket.core.ports.in.changeitems.ChangeItemsCommandHandler;
import com.example.basket.core.ports.out.BasketRepository;
import com.example.basket.core.ports.out.GoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChangeItemsCommandHandlerImpl implements ChangeItemsCommandHandler {
    private final BasketRepository basketRepository;
    private final GoodRepository goodRepository;

    @Override
    public void handle(ChangeItemsCommand command) {
        if (!basketRepository.exists(command.getBasketId())) {
            var basket = Basket.of(command.getBasketId(), command.getBuyerId());
            basketRepository.create(basket);
        }

        var basket = basketRepository.get(command.getBasketId());
        var good = goodRepository.get(command.getGoodId());
        basket.change(good, command.getQuantity());
        basketRepository.update(basket);
    }
}
