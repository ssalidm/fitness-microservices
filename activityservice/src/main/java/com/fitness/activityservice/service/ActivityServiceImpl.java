package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;

    @Override
    public ActivityResponse trackActivity(ActivityRequest request) {
        Activity savedActivity = activityRepository.save(toEntity(request));
        return ActivityResponse.from(savedActivity);
    }

    @Override
    public List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activities =  activityRepository.findByUserId(userId);
        return activities.stream().map(ActivityResponse::from).collect(Collectors.toList());
    }

    @Override
    public ActivityResponse getActivity(String activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(
                () -> new IllegalArgumentException("Activity not found with id: " + activityId));
        return ActivityResponse.from(activity);
    }

    private static Activity toEntity(ActivityRequest request) {
        return Activity.builder()
                .userId(request.userId())
                .type(request.type())
                .duration(request.duration())
                .caloriesBurned(request.caloriesBurned())
                .startTime(request.startTime())
                .additionalMetrics(request.additionalMetrics())
                .build();
    }
}
