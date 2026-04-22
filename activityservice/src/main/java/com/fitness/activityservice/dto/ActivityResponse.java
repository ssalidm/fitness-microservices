package com.fitness.activityservice.dto;

import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.model.ActivityType;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

public record ActivityResponse(
        String id,
        String userId,
        ActivityType type,
        Integer duration,
        Integer caloriesBurned,
        LocalDateTime startTime,
        Map<String, Object> additionalMetrics,
        Instant createdAt,
        Instant updatedAt
) {
    public static ActivityResponse from(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getUserId(),
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getStartTime(),
                activity.getAdditionalMetrics(),
                activity.getCreatedAt(),
                activity.getUpdatedAt()
        );
    }
}
