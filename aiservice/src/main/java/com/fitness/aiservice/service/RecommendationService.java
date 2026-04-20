package com.fitness.aiservice.service;

import com.fitness.aiservice.dto.RecommendationResponse;

import java.util.List;

public interface RecommendationService {
    List<RecommendationResponse> getUserRecommendation(String userId);

    RecommendationResponse getActivityRecommendation(String activityId);
}
