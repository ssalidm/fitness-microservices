package com.fitness.api.gateway.dto;

import java.time.Instant;

public record UserResponse(
        String id,
        String keycloakId,
        String email,
        String firstName,
        String lastName,
        UserRole role,
        Instant createdAt,
        Instant updatedAt
) {}
