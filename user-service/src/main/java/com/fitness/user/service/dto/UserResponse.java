package com.fitness.user.service.dto;


import com.fitness.user.service.model.User;
import com.fitness.user.service.model.UserRole;

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
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getKeycloakId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
