package com.fitness.activityservice.controller;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.common.dto.ApiResponse;
import com.fitness.activityservice.service.ActivityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivitiesController {

    private final ActivityService activityService;

    @PostMapping
    public ResponseEntity<ApiResponse<ActivityResponse>> trackActivity(@Valid @RequestBody ActivityRequest request) {
        return ResponseEntity.ok(ApiResponse.success(
                activityService.trackActivity(request),
                "Activity track successful",
                HttpStatus.CREATED.value()
        ));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ActivityResponse>>> getUserActivities(
            @RequestHeader(value = "X-User-ID") String userId) {
        return ResponseEntity.ok(ApiResponse.success(
                activityService.getUserActivities(userId),
                "Activities retrieved successfully"
        ));
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<ApiResponse<ActivityResponse>> getActivity(@PathVariable String activityId) {
        return ResponseEntity.ok(ApiResponse.success(
                activityService.getActivity(activityId),
                "Activity retrieved successfully"
        ));
    }
}
