package com.dbs.account.application.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record MoneyTransferRequest(
        String fromIban,
        String toIban,
        @Positive(message = "Transfer amount must be positive")
        BigDecimal amount
) {}