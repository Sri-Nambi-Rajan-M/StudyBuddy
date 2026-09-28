package com.StudyBuddy.StudyBuddy.exception;

import com.StudyBuddy.StudyBuddy.dto.ErrorResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public class BussinessRuleException extends RuntimeException {

    private final HttpStatus status;

    // Default: business rule violation -> 409 Conflict
    public BussinessRuleException(String message) {
        this(HttpStatus.CONFLICT, message);
    }

    public BussinessRuleException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public static BussinessRuleException notFound(String message) {
        return new BussinessRuleException(HttpStatus.NOT_FOUND, message);
    }

    public static BussinessRuleException forbidden(String message) {
        return new BussinessRuleException(HttpStatus.FORBIDDEN, message);
    }

    // Global handler: turns exceptions into clear JSON errors instead of stack traces
    @RestControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(BussinessRuleException.class)
        public ResponseEntity<ErrorResponse> handleBusiness(BussinessRuleException ex) {
            return build(ex.getStatus(), ex.getMessage(), null);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
            Map<String, String> errors = new LinkedHashMap<>();
            ex.getBindingResult().getFieldErrors()
                    .forEach(fe -> errors.put(fe.getField(), fe.getDefaultMessage()));
            return build(HttpStatus.BAD_REQUEST, "Validation failed", errors);
        }

        @ExceptionHandler({
                HttpMessageNotReadableException.class,
                MethodArgumentTypeMismatchException.class,
                MissingServletRequestParameterException.class
        })
        public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
            return build(HttpStatus.BAD_REQUEST, "Malformed request or missing/invalid parameter", null);
        }

        // Safety net: DB unique constraints still protect data if two requests race
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<ErrorResponse> handleIntegrity(DataIntegrityViolationException ex) {
            return build(HttpStatus.CONFLICT, "Request conflicts with existing data (duplicate entry)", null);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
            return build(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong. Please try again later.", null);
        }

        private ResponseEntity<ErrorResponse> build(HttpStatus status, String message, Map<String, String> fieldErrors) {
            ErrorResponse body = new ErrorResponse(
                    LocalDateTime.now(), status.value(), status.getReasonPhrase(), message, fieldErrors);
            return ResponseEntity.status(status).body(body);
        }
    }
}