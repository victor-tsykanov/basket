package com.example.basket.core.application.usecases.commands;

import com.example.basket.core.domain.kernel.Address;
import com.example.basket.core.ports.in.addaddress.AddAddressCommand;
import com.example.basket.core.ports.in.addaddress.AddAddressCommandHandler;
import com.example.basket.core.ports.out.BasketRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AddAddressCommandHandlerImpl implements AddAddressCommandHandler {
    private final BasketRepository basketRepository;

    @Override
    public void handle(AddAddressCommand command) {
        var address = Address.of(
                command.getCountry(),
                command.getStreet(),
                command.getHouse(),
                command.getApartment()
        );

        var basket = basketRepository.get(command.getBasketId());
        basket.addAddress(address);
        basketRepository.update(basket);
    }
}
