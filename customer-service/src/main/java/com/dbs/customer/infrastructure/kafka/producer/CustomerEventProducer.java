package com.dbs.customer.infrastructure.kafka.producer;

import com.dbs.customer.domain.event.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerEventProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${dbs.kafka.topics.customer-created}")
    private String customerCreatedTopic;

    public void sendCustomerCreatedEvent(CustomerCreatedEvent event) {
        log.info("Sending customer created event to topic: {}, payload: {}", customerCreatedTopic, event);
        kafkaTemplate.send(customerCreatedTopic, event.customerNumber(), event);
    }
}
