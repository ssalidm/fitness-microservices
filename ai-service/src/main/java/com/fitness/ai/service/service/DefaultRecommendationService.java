package com.fitness.ai.service.service;

import com.fitness.ai.service.dto.RecommendationResponse;
import com.fitness.ai.service.model.Recommendation;
import com.fitness.ai.service.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultRecommendationService implements RecommendationService {

    private final RecommendationRepository recommendationRepository;

    @Override
    public List<RecommendationResponse> getUserRecommendation(String userId) {
        return recommendationRepository.findByUserId(userId).stream()
                .map(RecommendationResponse::from).collect(Collectors.toList());
    }

    @Override
    public RecommendationResponse getActivityRecommendation(String activityId) {
        Recommendation recommendation = recommendationRepository.findByActivityId(activityId)
                .orElseThrow(() -> new IllegalArgumentException("No recommendation found for this activity: " + activityId)
        );

        return RecommendationResponse.from(recommendation);
    }
}
