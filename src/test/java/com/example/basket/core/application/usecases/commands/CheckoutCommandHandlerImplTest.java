package com.example.basket.core.application.usecases.commands;

import com.example.basket.core.domain.model.basket.Basket;
import com.example.basket.core.domain.services.PromoGoodsService;
import com.example.basket.core.ports.in.chechout.CheckoutCommand;
import com.example.basket.core.ports.out.BasketRepository;
import com.example.basket.factories.BasketFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class CheckoutCommandHandlerImplTest {
    @Test
    void handle() {
        // Arrange
        var basket = BasketFactory.buildReadyForCheckout();

        var basketRepository = mock(BasketRepository.class);
        when(basketRepository.get(basket.getId())).thenReturn(basket);

        var promoGoodsService = mock(PromoGoodsService.class);

        var command = CheckoutCommand.of(basket.getId());
        var handler = new CheckoutCommandHandlerImpl(basketRepository, promoGoodsService);

        // Act
        handler.handle(command);

        // Assert
        verify(promoGoodsService).addPromo(
                assertArg(b -> {
                    assertThat(b.getId()).isEqualTo(basket.getId());
                })
        );
        verify(basketRepository).update(
                assertArg(b -> {
                    assertThat(b.getId()).isEqualTo(basket.getId());
                    assertThat(b.getStatus()).isEqualTo(Basket.Status.CONFIRMED);
                })
        );
    }
}
