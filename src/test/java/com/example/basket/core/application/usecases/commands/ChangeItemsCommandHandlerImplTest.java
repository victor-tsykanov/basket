package com.example.basket.core.application.usecases.commands;

import com.example.basket.core.domain.model.basket.Item;
import com.example.basket.core.ports.in.changeitems.ChangeItemsCommand;
import com.example.basket.core.ports.out.BasketRepository;
import com.example.basket.core.ports.out.GoodRepository;
import com.example.basket.factories.BasketFactory;
import com.example.basket.factories.GoodFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.Mockito.*;

class ChangeItemsCommandHandlerImplTest {
    @Test
    void handle_should_updateBasket_when_basketExists() {
        // Arrange
        var basket = BasketFactory.buildUnconfirmed();
        var good = GoodFactory.build();

        var basketRepository = mock(BasketRepository.class);
        when(basketRepository.get(basket.getId())).thenReturn(basket);
        when(basketRepository.exists(basket.getId())).thenReturn(true);

        var goodRepository = mock(GoodRepository.class);
        when(goodRepository.get(good.getId())).thenReturn(good);

        var command = ChangeItemsCommand.of(basket.getId(), basket.getBuyerId(), good.getId(), 3);
        var handler = new ChangeItemsCommandHandlerImpl(basketRepository, goodRepository);

        // Act
        handler.handle(command);

        // Assert
        verify(basketRepository).update(
                assertArg(b -> {
                    assertThat(b.getId()).isEqualTo(basket.getId());
                    assertThat(b.getItems())
                            .extracting(Item::getGoodId, Item::getQuantity)
                            .containsExactly(
                                    tuple(good.getId(), 3)
                            );
                })
        );
    }

    @Test
    void handle_should_createBasket_when_basketDoesNotExist() {
        // Arrange
        var basket = BasketFactory.buildUnconfirmed();
        var good = GoodFactory.build();

        var basketRepository = mock(BasketRepository.class);
        when(basketRepository.exists(basket.getId())).thenReturn(false);
        when(basketRepository.get(basket.getId())).thenReturn(basket);

        var goodRepository = mock(GoodRepository.class);
        when(goodRepository.get(good.getId())).thenReturn(good);

        var command = ChangeItemsCommand.of(basket.getId(), basket.getBuyerId(), good.getId(), 3);
        var handler = new ChangeItemsCommandHandlerImpl(basketRepository, goodRepository);

        // Act
        handler.handle(command);

        // Assert
        verify(basketRepository).create(
                assertArg(b -> {
                    assertThat(b.getId()).isEqualTo(basket.getId());
                })
        );

        verify(basketRepository).update(
                assertArg(b -> {
                    assertThat(b.getId()).isEqualTo(basket.getId());
                    assertThat(b.getItems())
                            .extracting(Item::getGoodId, Item::getQuantity)
                            .containsExactly(
                                    tuple(good.getId(), 3)
                            );
                })
        );
    }
}
