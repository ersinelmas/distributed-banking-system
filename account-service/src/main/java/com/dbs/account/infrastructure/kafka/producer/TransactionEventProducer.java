package com.dbs.account.infrastructure.kafka.producer;

import com.dbs.account.domain.event.MoneyTransferredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${dbs.kafka.topics.money-transferred:dbs.money.transferred.v1}")
    private String topic;

    public void sendTransactionEvent(MoneyTransferredEvent event) {
        log.info("Sending transaction event to Kafka for IBAN: {}", event.fromIban());
        kafkaTemplate.send(topic, event.fromIban(), event);
    }
}