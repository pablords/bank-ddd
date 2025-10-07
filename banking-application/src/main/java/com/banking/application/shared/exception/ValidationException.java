package com.banking.application.shared.exception;

import java.util.List;
import java.util.Map;

/**
 * Exceção lançada quando ocorrem erros de validação na camada de aplicação.
 */
public class ValidationException extends ApplicationException {

    private final List<String> validationErrors;

    public ValidationException(String message) {
        super(message, "VALIDATION_ERROR");
        this.validationErrors = List.of(message);
    }

    public ValidationException(List<String> validationErrors) {
        super("Validation failed: " + String.join(", ", validationErrors), "VALIDATION_ERROR");
        this.validationErrors = List.copyOf(validationErrors);
    }

    public ValidationException(String field, String message) {
        super(String.format("Validation failed for field '%s': %s", field, message), "VALIDATION_ERROR");
        this.validationErrors = List.of(String.format("%s: %s", field, message));
    }

    public ValidationException(Map<String, String> fieldErrors) {
        super("Multiple validation errors", "VALIDATION_ERROR", fieldErrors);
        this.validationErrors = fieldErrors.entrySet().stream()
                .map(entry -> String.format("%s: %s", entry.getKey(), entry.getValue()))
                .toList();
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }

    public boolean hasValidationErrors() {
        return validationErrors != null && !validationErrors.isEmpty();
    }

    public int getValidationErrorCount() {
        return validationErrors != null ? validationErrors.size() : 0;
    }
}