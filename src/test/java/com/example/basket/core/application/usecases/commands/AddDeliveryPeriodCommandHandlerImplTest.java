package com.example.basket.core.application.usecases.commands;

import com.example.basket.core.domain.model.basket.DeliveryPeriod;
import com.example.basket.core.ports.in.adddeliveryperiod.AddDeliveryPeriodCommand;
import com.example.basket.core.ports.out.BasketRepository;
import com.example.basket.factories.BasketFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AddDeliveryPeriodCommandHandlerImplTest {
    @Test
    void handle_should_updateDeliveryPeriod() {
        // Arrange
        var basket = BasketFactory.buildUnconfirmed();

        var basketRepository = mock(BasketRepository.class);
        when(basketRepository.get(basket.getId())).thenReturn(basket);

        var command = AddDeliveryPeriodCommand.of(basket.getId(), "morning");
        var handler = new AddDeliveryPeriodCommandHandlerImpl(basketRepository);

        // Act
        handler.handle(command);

        // Assert
        verify(basketRepository).update(
                assertArg(b -> {
                    assertThat(b.getId()).isEqualTo(basket.getId());
                    assertThat(b.getDeliveryPeriod()).isEqualTo(DeliveryPeriod.MORNING);
                })
        );
    }
}
