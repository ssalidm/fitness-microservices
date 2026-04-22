package com.fitness.aiservice.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@ConfigurationProperties(prefix = "gemini.api")
@Validated
public record GeminiProperties(
        @NotBlank String url,
        @NotBlank String key
) {}
