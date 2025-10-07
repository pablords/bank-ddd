package com.banking.domain.shared.exception;

/**
 * Exceção base para todas as exceções de domínio.
 * Representa violações de regras de negócio ou estados inválidos.
 */
public abstract class DomainException extends RuntimeException {

    private final String errorCode;

    protected DomainException(String message) {
        super(message);
        this.errorCode = generateErrorCode();
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = generateErrorCode();
    }

    protected DomainException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    protected DomainException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
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