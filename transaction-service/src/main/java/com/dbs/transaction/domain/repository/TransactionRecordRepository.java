package com.dbs.transaction.domain.repository;

import com.dbs.transaction.domain.model.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRecordRepository extends JpaRepository<TransactionRecord, String> {
    List<TransactionRecord> findByFromIbanOrToIbanOrderByTransactionDateDesc(String fromIban, String toIban);
}