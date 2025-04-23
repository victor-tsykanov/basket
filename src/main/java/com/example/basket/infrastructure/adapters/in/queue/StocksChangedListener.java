package com.example.basket.infrastructure.adapters.in.queue;

import com.example.basket.core.ports.in.changestocks.ChangeStocksCommand;
import com.example.basket.core.ports.in.changestocks.ChangeStocksCommandHandler;
import com.example.warehouse.StocksChanged.StocksChangedIntegrationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class StocksChangedListener {
    private final ChangeStocksCommandHandler changeStocksCommandHandler;

    @KafkaListener(topics = "${kafka.stocks-changed-topic}")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received message: {}", record);

        var event = parseEvent(record.value());
        var command = ChangeStocksCommand.of(
                UUID.fromString(event.getGoodId()),
                event.getQuantity()
        );
        changeStocksCommandHandler.handle(command);
    }

    private StocksChangedIntegrationEvent parseEvent(String message) {
        var mapper = new ObjectMapper();
        Map<String, Object> values;

        try {
            values = mapper.readValue(message, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var goodId = (String) values.get("goodId");
        var quantity = (Integer) values.get("quantity");

        return StocksChangedIntegrationEvent
                .newBuilder()
                .setGoodId(Objects.requireNonNull(goodId))
                .setQuantity(Objects.requireNonNull(quantity))
                .build();
    }
}
