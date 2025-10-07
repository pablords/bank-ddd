package com.banking.application.shared.exception;

/**
 * Exceção base para todas as exceções da camada de aplicação.
 */
public abstract class ApplicationException extends RuntimeException {

    private final String errorCode;
    private final Object details;

    protected ApplicationException(String message) {
        super(message);
        this.errorCode = generateErrorCode();
        this.details = null;
    }

    protected ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = generateErrorCode();
        this.details = null;
    }

    protected ApplicationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.details = null;
    }

    protected ApplicationException(String message, String errorCode, Object details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    protected ApplicationException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = null;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Object getDetails() {
        return details;
    }

    /**
     * Gera código de erro baseado no nome da classe
     */
    private String generateErrorCode() {
        return this.getClass().getSimpleName().toUpperCase().replace("EXCEPTION", "_ERROR");
    }

    @Override
    public String toString() {
        return String.format("%s[%s]: %s", 
            getClass().getSimpleName(), errorCode, getMessage());
    }
}