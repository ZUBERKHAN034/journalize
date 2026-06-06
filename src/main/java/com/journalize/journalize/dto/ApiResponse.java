package com.journalize.journalize.dto;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private String message;
    private T data;
    private String error;
    private Map<String, String> errors;

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(message, null, null, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data, null, null);
    }

    public static <T> ApiResponse<T> error(String error) {
        return new ApiResponse<>(null, null, error, null);
    }

    public static <T> ApiResponse<T> validationErrors(Map<String, String> errors) {
        return new ApiResponse<>(null, null, null, errors);
    }
}