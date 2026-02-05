package com.dbs.customer.application.dto;

import java.time.LocalDateTime;

public record CustomerResponse(
        String customerNumber,
        String firstName,
        String lastName,
        String email,
        String status,
        LocalDateTime createdAt
) {}