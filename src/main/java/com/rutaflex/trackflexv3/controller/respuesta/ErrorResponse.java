package com.rutaflex.trackflexv3.controller.respuesta;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private int status;
    private String error;
    private String message;
    private String path;
    private String method;
    private LocalDateTime timestamp = LocalDateTime.now();

    public ErrorResponse(int status, String error, String message, String path, String method) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.method = method;
    }

    // Getters y setters (o usa lombok @Data para simplificar)
}