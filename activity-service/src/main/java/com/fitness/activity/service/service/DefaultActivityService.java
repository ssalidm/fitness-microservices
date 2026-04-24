package com.fitness.activity.service.service;

import com.fitness.activity.service.config.RabbitMqConfig;
import com.fitness.activity.service.dto.ActivityRequest;
import com.fitness.activity.service.dto.ActivityResponse;
import com.fitness.activity.service.model.Activity;
import com.fitness.activity.service.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DefaultActivityService implements ActivityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultActivityService.class);
    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;
    private final RabbitTemplate rabbitTemplate;


    @Override
    public ActivityResponse trackActivity(ActivityRequest request) {
        boolean isValidUser = userValidationService.validateUser(request.userId());
        if (!isValidUser) throw new IllegalArgumentException("Invalid user: " + request.userId());
        Activity savedActivity = activityRepository.save(toEntity(request));

        // Publish to RabbitMQ for AI Processing
        publishToRabbitMq(savedActivity);

        return ActivityResponse.from(savedActivity);
    }


    @Override
    public List<ActivityResponse> getUserActivities(String userId) {
        boolean isValidUser = userValidationService.validateUser(userId);
        if (!isValidUser) throw new IllegalArgumentException("Invalid user: " + userId);
        List<Activity> activities =  activityRepository.findByUserId(userId);
        return activities.stream().map(ActivityResponse::from).collect(Collectors.toList());
    }

    @Override
    public ActivityResponse getActivity(String activityId) {
        Activity activity = activityRepository.findById(activityId).orElseThrow(
                () -> new IllegalArgumentException("Activity not found with id: " + activityId));
        return ActivityResponse.from(activity);
    }


    // Helper methods

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

    private void publishToRabbitMq(Activity savedActivity) {
        try {
            LOGGER.info("➡️➡️➡️ [ACTIVITY-SERVICE] Dispatching activity '{}' to the message broker...", savedActivity.getId());
            rabbitTemplate.convertAndSend(
                    RabbitMqConfig.ACTIVITY_EXCHANGE,
                    RabbitMqConfig.ACTIVITY_ROUTING_KEY,
                    savedActivity
            );
            LOGGER.info("✅ [ACTIVITY-SERVICE] Activity '{}' successfully queued!", savedActivity.getId());
        } catch (Exception ex) {
            LOGGER.error("❗Failed to publish activity to RabbitMQ: ", ex);
        }
    }
}
