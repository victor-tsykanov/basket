package com.example.basket.core.application.usecases.commands;

import com.example.basket.core.domain.kernel.Address;
import com.example.basket.core.domain.kernel.Discount;
import com.example.basket.core.domain.model.basket.Basket;
import com.example.basket.core.domain.model.basket.DeliveryPeriod;
import com.example.basket.core.ports.in.testcheckout.TestCheckoutCommand;
import com.example.basket.core.ports.in.testcheckout.TestCheckoutCommandHandler;
import com.example.basket.core.ports.out.BasketRepository;
import com.example.basket.core.ports.out.GoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class TestCheckoutCommandHandlerImpl implements TestCheckoutCommandHandler {
    private final GoodRepository goodRepository;
    private final BasketRepository basketRepository;

    @Override
    public void handle(TestCheckoutCommand command) {
        var basket = Basket.of(UUID.randomUUID());

        var random = new Random();
        var allGoods = goodRepository.findAll();

        IntStream
                .of(random.nextInt(1, 10))
                .forEach((i) -> {
                    var good = allGoods.get(random.nextInt(allGoods.size()));
                    basket.change(good, random.nextInt(1, 10));
                });

        var address = Address.of(
                "Indonesia",
                "Sukabumi",
                "Wora Wari",
                "217",
                "587"
        );
        basket.addAddress(address);

        var deliveryPeriod = DeliveryPeriod.LIST.get(random.nextInt(DeliveryPeriod.LIST.size()));
        basket.addDeliveryPeriod(deliveryPeriod);

        basket.checkout(Discount.of(random.nextDouble(0, 100)));

        basketRepository.create(basket);
    }
}
