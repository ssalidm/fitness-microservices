package com.fitness.activityservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static String ACTIVITY_QUEUE;
    public static String ACTIVITY_EXCHANGE;
    public static String ACTIVITY_ROUTING_KEY;

    @Value("${app.rabbitmq.queue.name}")
    public void setActivityQueue(String activityQueue) {
        ACTIVITY_QUEUE = activityQueue;
    }

    @Value("${app.rabbitmq.exchange.name}")
    public void setActivityExchange(String activityExchange) {
        ACTIVITY_EXCHANGE = activityExchange;
    }

    @Value("${app.rabbitmq.routing.key}")
    public void setActivityRoutingKey(String activityRoutingKey) {
        ACTIVITY_ROUTING_KEY = activityRoutingKey;
    }


    @Bean
    public Queue activityQueue() {
        return new Queue(ACTIVITY_QUEUE, true);
    }

    @Bean
    public DirectExchange activityExchange() {
        return new DirectExchange(ACTIVITY_EXCHANGE);
    }

    @Bean
    public Binding activityBinding() {
        return BindingBuilder.bind(activityQueue()).to(activityExchange()).with(ACTIVITY_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
