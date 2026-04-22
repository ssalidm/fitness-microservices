package com.fitness.aiservice.controller;

import com.fitness.aiservice.dto.RecommendationResponse;
import com.fitness.aiservice.service.RecommendationService;
import com.fitness.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("users/{userId}")
    public ResponseEntity<ApiResponse<List<RecommendationResponse>>> getUserRecommendation(@PathVariable String userId) {
        return ResponseEntity.ok(ApiResponse.success(
                recommendationService.getUserRecommendation(userId),
                "User recommendations retrieved successfully"
        ));
    }

    @GetMapping("activities/{activityId}")
    public ResponseEntity<ApiResponse<RecommendationResponse>> getActivityRecommendation(@PathVariable String activityId) {
        return ResponseEntity.ok(ApiResponse.success(
                recommendationService.getActivityRecommendation(activityId),
                "Activity recommendation retrieved successfully"
        ));
    }
}
