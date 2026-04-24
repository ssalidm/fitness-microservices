package com.fitness.user.service.service;

import com.fitness.user.service.dto.RegisterRequest;
import com.fitness.user.service.dto.UserResponse;

public interface UserService {
    UserResponse register(RegisterRequest request);

    UserResponse getUserProfile(String userId);

    boolean existsByKeycloakId(String keycloakId);
}
