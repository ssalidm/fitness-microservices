package com.fitness.user.service.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        String keycloakId,

        @NotBlank(message = "First Name is Required")
        @Size(min = 3, message = "First Name must have a minimum of 3 characters")
        String firstName,

        @NotBlank(message = "Last Name is Required")
        @Size(min = 3, message = "Last Name must have a minimum of 3 characters")
        String lastName
) {}
