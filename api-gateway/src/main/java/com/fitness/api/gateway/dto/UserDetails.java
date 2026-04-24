package com.fitness.api.gateway.dto;

import lombok.*;

@Builder
public record UserDetails (
    String keycloakId,
    String email,
    String firstName,
    String lastName
) {}
