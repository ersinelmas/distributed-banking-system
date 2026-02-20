package com.dbs.notification.infrastructure.kafka.consumer;

import com.dbs.notification.domain.event.CustomerCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomerEventConsumer {

    @KafkaListener(
            topics = "${dbs.kafka.topics.customer-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeCustomerCreatedEvent(CustomerCreatedEvent event) {
        log.info("Event consumed: Customer created with number {}", event.customerNumber());
        log.info("Sending welcome email to {} for customer {} {}",
                event.email(), event.firstName(), event.lastName());
    }
}