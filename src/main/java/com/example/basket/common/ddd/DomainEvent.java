package com.example.basket.common.ddd;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public abstract class DomainEvent extends ApplicationEvent {
    private final UUID id;
    private final String type;

    public DomainEvent(Object source, UUID id, String type) {
        super(source);
        this.id = id;
        this.type = type;
    }
}
