package com.dbs.customer.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CustomerCreateRequest(
        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 10, max = 11)
        String taxNumber
) {}