package com.example.basket.core.application.usecases.events;

import com.example.basket.core.domain.model.basket.events.BasketConfirmedEvent;
import com.example.basket.infrastructure.adapters.out.queue.BasketConfirmedEventPublisherImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class BasketConfirmedEventListener implements ApplicationListener<BasketConfirmedEvent> {
    private final BasketConfirmedEventPublisherImpl eventPublisher;

    @Override
    public void onApplicationEvent(BasketConfirmedEvent event) {
        eventPublisher.publish(event);
    }
}
