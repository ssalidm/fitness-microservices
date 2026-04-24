package com.fitness.user.service.controller;

import com.fitness.common.dto.ApiResponse;
import com.fitness.user.service.dto.RegisterRequest;
import com.fitness.user.service.dto.UserResponse;
import com.fitness.user.service.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserProfile(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.success(
                userService.getUserProfile(userId),
                "User profile retrieved successfully"
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                userService.register(request),
                "User registration successful",
                HttpStatus.CREATED.value()
        ));
    }

    @GetMapping("/{keycloakId}/validate")
    public ResponseEntity<Boolean> validateUser(@PathVariable String keycloakId) {
        return ResponseEntity.ok(userService.existsByKeycloakId(keycloakId));
    }
}
