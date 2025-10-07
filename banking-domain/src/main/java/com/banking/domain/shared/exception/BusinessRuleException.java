package com.banking.domain.shared.exception;

/**
 * Exceção lançada quando uma regra de negócio é violada.
 */
public class BusinessRuleException extends DomainException {

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, String errorCode) {
        super(message, errorCode);
    }

    public BusinessRuleException(String message, Throwable cause) {
        super(message, cause);
    }
}