package com.dbs.customer.infrastructure.kafka.consumer;

import com.dbs.customer.domain.event.CustomerCreatedEvent;
import com.dbs.customer.domain.model.elastic.CustomerIndex;
import com.dbs.customer.domain.repository.elastic.CustomerElasticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerSearchConsumer {

    private final CustomerElasticRepository elasticRepository;

    @KafkaListener(
            topics = "${dbs.kafka.topics.customer-created}",
            groupId = "search-group"
    )
    public void consumeCustomerCreatedEvent(CustomerCreatedEvent event) {
        log.info("Indexing customer to ElasticSearch: {}", event.customerNumber());

        CustomerIndex customerIndex = CustomerIndex.builder()
                .customerNumber(event.customerNumber())
                .firstName(event.firstName())
                .lastName(event.lastName())
                .email(event.email())
                .build();

        elasticRepository.save(customerIndex);
    }
}