package com.example.basket.core.application.usecases.commands;

import com.example.basket.core.ports.in.changestocks.ChangeStocksCommand;
import com.example.basket.core.ports.out.GoodRepository;
import com.example.basket.factories.GoodFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ChangeStocksCommandHandlerImplTest {
    @Test
    void handle() {
        // Arrange
        var good = GoodFactory.build();

        var goodRepository = mock(GoodRepository.class);
        when(goodRepository.get(good.getId())).thenReturn(good);

        var newQuantity = good.getQuantity() + 1;
        var command = ChangeStocksCommand.of(good.getId(), newQuantity);
        var handler = new ChangeStocksCommandHandlerImpl(goodRepository);

        // Act
        handler.handle(command);

        // Assert
        verify(goodRepository).update(
                assertArg(g -> {
                    assertThat(g.getId()).isEqualTo(good.getId());
                    assertThat(g.getQuantity()).isEqualTo(newQuantity);
                })
        );
    }
}
