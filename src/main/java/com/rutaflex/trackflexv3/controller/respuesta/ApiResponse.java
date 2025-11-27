package com.rutaflex.trackflexv3.controller.respuesta;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiResponse<T>  {
    private LocalDateTime timestamp;
    private int status;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(String msg, T data) {
        return new ApiResponse<>(LocalDateTime.now(), 200, msg, data);
    }
    public static <T> ApiResponse<T> error(int status, String msg, T data) {
        return new ApiResponse<>(LocalDateTime.now(), status, msg, data);
    }
}