package com.dbs.account.application.dto;

import com.dbs.account.domain.model.AccountStatus;
import com.dbs.account.domain.model.Currency;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(
        String iban,
        String customerNumber,
        BigDecimal balance,
        Currency currency,
        AccountStatus status,
        LocalDateTime createdAt
) {}