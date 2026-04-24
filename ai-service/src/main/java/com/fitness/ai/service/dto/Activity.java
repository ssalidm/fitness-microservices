package com.fitness.ai.service.dto;


import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

public record Activity(
        String id,
        String userId,
        String type,
        Integer duration,
        Integer caloriesBurned,
        LocalDateTime startTime,
        Map<String, Object> additionalMetrics,
        Instant createdAt,
        Instant updatedAt
) {}