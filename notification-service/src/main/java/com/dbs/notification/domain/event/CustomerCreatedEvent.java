package com.dbs.notification.domain.event;

import java.time.LocalDateTime;

public record CustomerCreatedEvent(
        String customerNumber,
        String email,
        String firstName,
        String lastName,
        LocalDateTime createdAt
) {}