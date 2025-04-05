package com.example.basket.core.application.usecases.commands;

import com.example.basket.core.domain.model.basket.DeliveryPeriod;
import com.example.basket.core.ports.in.adddeliveryperiod.AddDeliveryPeriodCommand;
import com.example.basket.core.ports.in.adddeliveryperiod.AddDeliveryPeriodCommandHandler;
import com.example.basket.core.ports.out.BasketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddDeliveryPeriodCommandHandlerImpl implements AddDeliveryPeriodCommandHandler {
    private final BasketRepository basketRepository;

    @Override
    public void handle(AddDeliveryPeriodCommand command) {
        var basket = basketRepository.get(command.getBasketId());
        var deliveryPeriod = DeliveryPeriod.getByName(command.getDeliveryPeriod());
        basket.addDeliveryPeriod(deliveryPeriod);
        basketRepository.update(basket);
    }
}
