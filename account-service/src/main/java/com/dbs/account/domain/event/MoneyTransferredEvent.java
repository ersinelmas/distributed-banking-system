package com.dbs.account.domain.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MoneyTransferredEvent(
        String fromIban,
        String toIban,
        BigDecimal amount,
        String type,
        LocalDateTime transactionDate
) {}