package com.rutaflex.trackflexv3.controller.error;

import com.rutaflex.trackflexv3.controller.respuesta.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private <T> ResponseEntity<ApiResponse<T>> error(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status)
                .body(ApiResponse.error(status.value(), message, data));
    }

    // =======================================================
    // 🔥 1) ACCESS DENIED — DEBE ESTAR ARRIBA
    // =======================================================
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 403);
        body.put("message", "No tienes permisos para realizar esta acción");

        Map<String, Object> data = new HashMap<>();
        data.put("message", ex.getMessage());
        data.put("path", request.getRequestURI());
        data.put("method", request.getMethod());
        body.put("data", data);

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    // =======================================================
    // 🔥 2) TOKEN EXPIRADO
    // =======================================================
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<?> handleExpiredToken(ExpiredJwtException ex, HttpServletRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", 401);
        body.put("message", "Tu token ha expirado. Inicia sesión nuevamente.");

        Map<String, Object> data = new HashMap<>();
        data.put("expiredAt", ex.getClaims().getExpiration());
        data.put("path", request.getRequestURI());
        data.put("method", request.getMethod());
        body.put("data", data);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    // =======================================================
    // 3) HANDLERS ANTERIORES (sin cambios)
    // =======================================================

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleNoHandler(NoHandlerFoundException ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("path", ex.getRequestURL());
        details.put("method", ex.getHttpMethod());
        return error(HttpStatus.NOT_FOUND, "La ruta solicitada no existe.", details);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleUnreadable(HttpMessageNotReadableException ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        String root = (ex.getMostSpecificCause() != null) ? ex.getMostSpecificCause().getMessage() : ex.getMessage();
        details.put("cause", safeMsg(root));
        return error(HttpStatus.BAD_REQUEST,
                "El cuerpo de la solicitud está malformado o contiene errores de formato.",
                details);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err ->
                fieldErrors.put(err.getField(), err.getDefaultMessage()));
        return error(HttpStatus.BAD_REQUEST, "Error de validación", fieldErrors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();
        return error(HttpStatus.BAD_REQUEST, "Parámetros inválidos", errors);
    }

    @ExceptionHandler({BusinessException.class, ServiceException.class})
    public ResponseEntity<ApiResponse<Void>> handleBusiness(RuntimeException ex) {
        return error(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), null);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleDataIntegrity(DataIntegrityViolationException ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("hint", "Verifique claves foráneas, unicidad o longitudes de columnas.");
        details.put("cause", safeMsg(ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage()));
        return error(HttpStatus.CONFLICT, "La operación viola restricciones de integridad.", details);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleMissingParam(MissingServletRequestParameterException ex) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("parameter", ex.getParameterName());
        details.put("expectedType", ex.getParameterType());
        return error(HttpStatus.BAD_REQUEST, "Falta un parámetro requerido.", details);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        Map<String, Object> details = new LinkedHashMap<>();
        details.put("message", ex.getMessage());
        details.put("path", req.getRequestURI());
        details.put("method", req.getMethod());
        return error(HttpStatus.NOT_FOUND, "Recurso no encontrado", details);
    }

    // =======================================================
    // 🔥 4) HANDLER GENÉRICO — AL FINAL
    // =======================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleGeneric(Exception ex, HttpServletRequest req) throws Exception {

        if (ex instanceof ResourceNotFoundException) {
            throw ex;
        }

        Map<String, Object> details = new LinkedHashMap<>();
        details.put("message", safeMsg(ex.getMessage()));
        details.put("path", req.getRequestURI());
        details.put("method", req.getMethod());
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", details);
    }

    private String safeMsg(String msg) {
        if (msg == null) return null;
        String trimmed = msg.replaceAll("[\\r\\n]+", " ").trim();
        return trimmed.length() > 400 ? trimmed.substring(0, 400) + "…" : trimmed;
    }
}

