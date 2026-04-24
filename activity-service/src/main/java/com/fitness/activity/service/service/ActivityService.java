package com.fitness.activity.service.service;

import com.fitness.activity.service.dto.ActivityRequest;
import com.fitness.activity.service.dto.ActivityResponse;

import java.util.List;

public interface ActivityService {
    ActivityResponse trackActivity(ActivityRequest request);

    List<ActivityResponse> getUserActivities(String userId);

    ActivityResponse getActivity(String activityId);
}
