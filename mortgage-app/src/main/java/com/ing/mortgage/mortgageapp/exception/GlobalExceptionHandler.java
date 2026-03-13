package com.ing.mortgage.mortgageapp.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.Locale;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        log.info("Validation failed: {}", ex.getBindingResult().getFieldErrors());
        List<String> messages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> {
                    String key = err.getDefaultMessage();   // e.g. "validation.income.required"
                    return messageSource.getMessage(key, null, Locale.getDefault());
                })
                .toList();


        ApiError error = new ApiError(messages, "REQUEST_VALIDATION_FAILED", Instant.now());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(MortgageValidationException.class)
    public ResponseEntity<ApiError> handleBusinessRule(MortgageValidationException ex) {
        log.warn("Business rule violation: {}", ex.getMessage());
        String resolvedMessage = messageSource.getMessage(ex.getMessage(), null, Locale.getDefault());

        ApiError error = new ApiError(List.of(resolvedMessage), "BUSINESS_RULE_VIOLATION", Instant.now());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        log.error("Unexpected error", ex);
        ApiError error = new ApiError(List.of("Unexpected error"), "INTERNAL_ERROR", Instant.now());
        return ResponseEntity.internalServerError().body(error);
    }
}