package com.fitness.userservice.common.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard response envelope for every API endpoint.
 *
 * @JsonInclude(NON_NULL) — Fields that are null are omitted from the JSON.
 * This keeps success responses clean (no "errors": null)
 * and error responses clean (no "data": null).
 */
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final int status;
    private final String message;
    private final T result;

    private final Map<String, String> errors;
    private final String path;

    @Builder.Default
    private final LocalDateTime timestamp = LocalDateTime.now();

    public static <T> ApiResponse<T> success(T result, String message, int status) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(status)
                .message(message)
                .result(result)
                .build();
    }

    public static <T> ApiResponse<T> success(T result, String message) {
        return success(result, message, HttpStatus.OK.value());
    }

    public static ApiResponse<Void> error(String message, int status, String path) {
        return ApiResponse.<Void>builder()
                .success(false)
                .status(status)
                .message(message)
                .path(path)
                .build();
    }
}
