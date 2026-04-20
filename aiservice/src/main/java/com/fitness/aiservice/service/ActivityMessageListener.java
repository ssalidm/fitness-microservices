package com.fitness.aiservice.service;

import com.fitness.aiservice.dto.Activity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityMessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityMessageListener.class);

    @RabbitListener(queues = "${app.rabbitmq.queue.name}")
    public void processActivity(Activity activity) {
        try {
            Thread.sleep(10000);
            LOGGER.info("✅ [AI-SERVICE] Received activity for processing: {}", activity.id());
        } catch (InterruptedException e) {
            LOGGER.error("❌ Error during activity Processing", e);
            Thread.currentThread().interrupt();
        }
    }
}
