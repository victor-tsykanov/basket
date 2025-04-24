package com.example.basket.infrastructure.adapters.in.queue;

import com.example.basket.core.ports.in.changestocks.ChangeStocksCommand;
import com.example.basket.core.ports.in.changestocks.ChangeStocksCommandHandler;
import com.example.warehouse.StocksChanged.StocksChangedIntegrationEvent;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class StocksChangedEventListener {
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

    private StocksChangedIntegrationEvent parseEvent(String jsonMessage) {
        var builder = StocksChangedIntegrationEvent.newBuilder();
        try {
            JsonFormat.parser().ignoringUnknownFields().merge(jsonMessage, builder);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }

        return builder.build();
    }
}
