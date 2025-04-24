package com.example.basket.infrastructure.adapters.out.queue;

import com.example.basket.core.domain.kernel.Address;
import com.example.basket.core.domain.model.basket.Basket;
import com.example.basket.core.domain.model.basket.Basket.Status;
import com.example.basket.core.domain.model.basket.DeliveryPeriod;
import com.example.basket.core.domain.model.basket.Item;
import com.example.basket.core.domain.model.basket.events.BasketConfirmedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class BasketConfirmedEventPublisherImplTest {
    @Test
    void publisher_should_sendMessage() {
        // Arrange
        var basket = Basket.restore(
                UUID.fromString("d368c04a-9f49-4b05-868f-9798d1774541"),
                UUID.randomUUID(),
                Status.CONFIRMED,
                List.of(
                        Item.restore(
                                UUID.fromString("4d619356-2c38-4b25-b85a-05b0cf08b38d"),
                                UUID.fromString("fa94af53-501c-4068-9b19-c3d14481063a"),
                                "Milk",
                                "...",
                                6.0,
                                7
                        )
                ),
                Address.of("Australia", "Mertzstad", "Hector Road", "2178", "99"),
                DeliveryPeriod.restore(1, "Morning", 12, 17),
                42
        );

        var event = new BasketConfirmedEvent(basket);
        var topic = "the-topic";

        KafkaTemplate<String, String> kafkaTemplate = mock();
        when(kafkaTemplate.send(eq(topic), anyString())).thenReturn(CompletableFuture.completedFuture(null));

        var publisher = new BasketConfirmedEventPublisherImpl(topic, kafkaTemplate);

        // Act
        publisher.publish(event);

        // Assert
        verify(kafkaTemplate).send(
                eq(topic),
                eqJson("""
                        {
                          "basketId": "d368c04a-9f49-4b05-868f-9798d1774541",
                          "address": {
                            "country": "Australia",
                            "city": "Mertzstad",
                            "street": "Hector Road",
                            "house": "2178",
                            "apartment": "99"
                          },
                          "items": [{
                            "id": "4d619356-2c38-4b25-b85a-05b0cf08b38d",
                            "goodId": "fa94af53-501c-4068-9b19-c3d14481063a",
                            "title": "Milk",
                            "price": 6.0,
                            "quantity": 7
                          }],
                          "deliveryPeriod": {
                            "from": 12,
                            "to": 17
                          }
                        }
                        """)
        );
    }

    private String eqJson(String expected) {
        var mapper = new ObjectMapper();

        return assertArg((String actual) -> {
            Assertions.assertEquals(mapper.readTree(expected), mapper.readTree(actual));
        });
    }
}
