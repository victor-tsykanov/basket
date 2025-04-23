package com.example.basket.infrastructure.adapters.in.queue;

import com.example.basket.core.ports.in.changestocks.ChangeStocksCommand;
import com.example.basket.core.ports.in.changestocks.ChangeStocksCommandHandler;
import com.example.basket.infrastructure.adapters.out.persistence.PersistenceTestsConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "${kafka.stocks-changed-topic}")
@Import(PersistenceTestsConfiguration.class)
class StocksChangedListenerTest {
    @Value("${kafka.stocks-changed-topic}")
    private String topic = "";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockitoBean
    private ChangeStocksCommandHandler changeStocksCommandHandler;

    @Test
    void listener_should_executeChangeStocksCommand_when_stocksChangedMessageIsReceived() throws JsonProcessingException {
        // Arrange
        var goodId = UUID.randomUUID();
        var quantity = new Random().nextInt(1, 500);
        var payload = new ObjectMapper().writeValueAsString(
                Map.of(
                        "goodId", goodId,
                        "quantity", quantity
                )
        );

        // Act
        var unused = kafkaTemplate.send(topic, payload);

        // Assert
        verify(changeStocksCommandHandler, timeout(5000)).handle(ChangeStocksCommand.of(goodId, quantity));
    }
}
