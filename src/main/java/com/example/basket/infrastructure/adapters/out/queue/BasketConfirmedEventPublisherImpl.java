package com.example.basket.infrastructure.adapters.out.queue;

import com.example.basket.BasketConfirmed.Address;
import com.example.basket.BasketConfirmed.BasketConfirmedIntegrationEvent;
import com.example.basket.BasketConfirmed.DeliveryPeriod;
import com.example.basket.BasketConfirmed.Item;
import com.example.basket.core.domain.model.basket.events.BasketConfirmedEvent;
import com.example.basket.core.ports.out.BasketConfirmedEventPublisher;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

@Component
public class BasketConfirmedEventPublisherImpl implements BasketConfirmedEventPublisher {
    private final String topic;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public BasketConfirmedEventPublisherImpl(
            @Value("${kafka.basket-confirmed-topic}") String topic,
            KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(BasketConfirmedEvent event) {
        var integrationEvent = toIntegrationEvent(event);
        var payload = toJson(integrationEvent);

        var future = kafkaTemplate.send(topic, payload);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private String toJson(MessageOrBuilder messageOrBuilder) {
        try {
            return JsonFormat.printer().print(messageOrBuilder);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }

    private BasketConfirmedIntegrationEvent toIntegrationEvent(BasketConfirmedEvent event) {
        var basket = event.getBasket();
        var address = Objects.requireNonNull(basket.getAddress());
        var deliveryPeriod = Objects.requireNonNull(basket.getDeliveryPeriod());

        return BasketConfirmedIntegrationEvent
                .newBuilder()
                .setBasketId(event.getBasket().getId().toString())
                .setAddress(
                        Address
                                .newBuilder()
                                .setCountry(address.getCountry())
                                .setCity(address.getCity())
                                .setStreet(address.getStreet())
                                .setHouse(address.getHouse())
                                .setApartment(address.getApartment())
                )
                .setDeliveryPeriod(
                        DeliveryPeriod
                                .newBuilder()
                                .setFrom(deliveryPeriod.getFrom())
                                .setTo(deliveryPeriod.getTo())
                )
                .addAllItems(
                        basket
                                .getItems()
                                .stream()
                                .map(
                                        item -> Item
                                                .newBuilder()
                                                .setId(item.getId().toString())
                                                .setTitle(item.getTitle())
                                                .setPrice(item.getPrice())
                                                .setQuantity(item.getQuantity())
                                                .setGoodId(item.getGoodId().toString())
                                                .build()
                                )
                                .toList()
                )
                .build();
    }
}
