package com.dbs.transaction.infrastructure.kafka.consumer;

import com.dbs.transaction.domain.event.MoneyTransferredEvent;
import com.dbs.transaction.domain.model.TransactionRecord;
import com.dbs.transaction.domain.model.TransactionType;
import com.dbs.transaction.domain.repository.TransactionRecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionEventConsumer {

    private final TransactionRecordRepository repository;

    @KafkaListener(
            topics = "${dbs.kafka.topics.money-transferred}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void consumeMoneyTransferredEvent(MoneyTransferredEvent event) {
        log.info("Received transaction event. From: {}, To: {}, Amount: {}",
                event.fromIban(), event.toIban(), event.amount());

        TransactionRecord record = TransactionRecord.builder()
                .fromIban(event.fromIban())
                .toIban(event.toIban())
                .amount(event.amount())
                .type(TransactionType.valueOf(event.type()))
                .transactionDate(event.transactionDate())
                .build();

        repository.save(record);
        log.info("Transaction saved to database successfully.");
    }
}