package com.fitness.user.service.service;

import com.fitness.user.service.dto.RegisterRequest;
import com.fitness.user.service.dto.UserResponse;
import com.fitness.user.service.model.User;
import com.fitness.user.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            User existingUser = userRepository.findByEmail(request.email());
            return UserResponse.from(existingUser);
        }

        User savedUser = userRepository.saveAndFlush(toEntity(request));
        return UserResponse.from(savedUser);
    }

    @Override
    public UserResponse getUserProfile(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("User not found"));
        return UserResponse.from(user);
    }

    @Override
    public boolean existsByKeycloakId(String keycloakId) {
        return userRepository.existsByKeycloakId(keycloakId);
    }

    private static User toEntity(RegisterRequest request) {
        return User.builder()
                .email(request.email())
                .keycloakId(request.keycloakId())
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();
    }
}
