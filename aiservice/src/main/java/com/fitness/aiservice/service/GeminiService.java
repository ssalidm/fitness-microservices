package com.fitness.aiservice.service;

import com.fitness.aiservice.config.GeminiProperties;
import com.fitness.aiservice.dto.Content;
import com.fitness.aiservice.dto.GeminiRequest;
import com.fitness.aiservice.dto.TextPart;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
@Slf4j
public class GeminiService {

    private final WebClient webClient;
    private final GeminiProperties properties;

    public GeminiService(WebClient.Builder webClientBuilder, GeminiProperties properties) {
        this.webClient = webClientBuilder.build();
        this.properties = properties;
    }

    public String getAnswer(String prompt) {
         GeminiRequest request = new GeminiRequest(
                List.of(
                        new Content(
                                List.of(
                                        new TextPart(prompt)
                                )
                        )
                )
        );

        log.info(":::: GEMINI URL {}", properties.url());

        return webClient.post()
                .uri(properties.url())
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .header("X-goog-api-key", properties.key())
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
