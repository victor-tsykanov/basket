package com.example.basket.core.application.usecases.commands;

import com.example.basket.core.ports.in.changestocks.ChangeStocksCommand;
import com.example.basket.core.ports.in.changestocks.ChangeStocksCommandHandler;
import com.example.basket.core.ports.out.GoodRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ChangeStocksCommandHandlerImpl implements ChangeStocksCommandHandler {
    private final GoodRepository goodRepository;

    @Override
    public void handle(ChangeStocksCommand command) {
        var good = goodRepository.get(command.getGoodId());
        good.changeStocks(command.getQuantity());
        goodRepository.update(good);
    }
}
