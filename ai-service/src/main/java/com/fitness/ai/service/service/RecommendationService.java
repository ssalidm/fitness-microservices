package com.fitness.ai.service.service;

import com.fitness.ai.service.dto.RecommendationResponse;

import java.util.List;

public interface RecommendationService {
    List<RecommendationResponse> getUserRecommendation(String userId);

    RecommendationResponse getActivityRecommendation(String activityId);
}
