package com.example.basket.core.application.usecases.commands;

import com.example.basket.core.domain.kernel.Address;
import com.example.basket.core.ports.in.addaddress.AddAddressCommand;
import com.example.basket.core.ports.out.BasketRepository;
import com.example.basket.factories.BasketFactory;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AddAddressCommandHandlerImplTest {
    net.datafaker.providers.base.Address addressFaker = new Faker().address();

    @Test
    void handle_should_updateAddress() {
        // Arrange
        var basket = BasketFactory.buildUnconfirmed();

        var basketRepository = mock(BasketRepository.class);
        when(basketRepository.get(basket.getId())).thenReturn(basket);

        var command = AddAddressCommand.of(
                basket.getId(),
                addressFaker.country(),
                addressFaker.city(),
                addressFaker.streetName(),
                addressFaker.buildingNumber(),
                addressFaker.secondaryAddress()
        );
        var handler = new AddAddressCommandHandlerImpl(basketRepository);

        // Act
        handler.handle(command);

        // Assert
        verify(basketRepository).update(
                assertArg(b -> {
                    assertThat(b.getId()).isEqualTo(command.getBasketId());
                    assertThat(b.getAddress()).isEqualTo(
                            Address.of(
                                    command.getCountry(),
                                    command.getCity(),
                                    command.getStreet(),
                                    command.getHouse(),
                                    command.getApartment()
                            )
                    );
                })
        );
    }
}
