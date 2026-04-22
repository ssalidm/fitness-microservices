package com.fitness.aiservice.dto;

import com.fitness.aiservice.model.Recommendation;

import java.time.Instant;
import java.util.List;

public record RecommendationResponse(
        String id,
        String activityId,
        String userId,
        String activityType,
        String recommendation,
        List<String> improvements,
        List<String> suggestions,
        List<String> safety,
        Instant createdAt
) {
    public static RecommendationResponse from(Recommendation recommendation) {
        return new RecommendationResponse(
                recommendation.getId(),
                recommendation.getActivityId(),
                recommendation.getUserId(),
                recommendation.getActivityType(),
                recommendation.getRecommendation(),
                recommendation.getImprovements(),
                recommendation.getSuggestions(),
                recommendation.getSafety(),
                recommendation.getCreatedAt()
        );
    }
}
