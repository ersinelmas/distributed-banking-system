package com.dbs.account.domain.event;

import java.time.LocalDateTime;

public record CustomerCreatedEvent(
        String customerNumber,
        String email,
        String firstName,
        String lastName,
        LocalDateTime createdAt
) {}