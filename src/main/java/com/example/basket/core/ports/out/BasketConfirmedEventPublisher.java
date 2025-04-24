package com.example.basket.core.ports.out;

import com.example.basket.core.domain.model.basket.events.BasketConfirmedEvent;

public interface BasketConfirmedEventPublisher {
    void publish(BasketConfirmedEvent event);
}
