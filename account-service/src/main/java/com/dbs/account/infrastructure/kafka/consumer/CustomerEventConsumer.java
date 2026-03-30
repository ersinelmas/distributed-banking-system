package com.dbs.account.infrastructure.kafka.consumer;

import com.dbs.account.application.service.AccountService;
import com.dbs.account.domain.event.CustomerCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerEventConsumer {

    private final AccountService accountService;

    @KafkaListener(
            topics = "${dbs.kafka.topics.customer-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeCustomerCreatedEvent(CustomerCreatedEvent event) {
        log.info("Account Service received CustomerCreatedEvent for customer: {}", event.customerNumber());

        accountService.createDefaultAccount(event.customerNumber());

        log.info("Default account successfully created for customer: {}", event.customerNumber());
    }
}