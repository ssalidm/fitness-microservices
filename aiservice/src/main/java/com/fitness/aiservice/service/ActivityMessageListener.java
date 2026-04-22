package com.fitness.aiservice.service;

import com.fitness.aiservice.dto.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityMessageListener.class);
    private final ActivityAIService activityAIService;
    private final RecommendationRepository recommendationRepository;

    @RabbitListener(queues = "${app.rabbitmq.queue.name}")
    public void processActivity(Activity activity) {
        try {
            LOGGER.info("✅ [AI-SERVICE] Received activity for processing: {}", activity.id());
            Recommendation recommendation = activityAIService.generateRecommendation(activity);
            recommendationRepository.save(recommendation);
        } catch (Exception e) {
            LOGGER.error("❌ Error during activity Processing", e);
        }
    }
}
