package com.example.basket.common.ddd;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot {
    private List<DomainEvent> domainEvents = new ArrayList<>();

    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }

    protected void raiseDomainEvent(DomainEvent event) {
        domainEvents.add(event);
    }
}
