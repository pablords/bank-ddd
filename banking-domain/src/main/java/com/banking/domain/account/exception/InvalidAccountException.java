package com.banking.domain.account.exception;

import com.banking.domain.shared.exception.DomainException;

/**
 * Exceção lançada quando operações inválidas são tentadas em uma conta.
 */
public class InvalidAccountException extends DomainException {

    public InvalidAccountException(String message) {
        super(message, "INVALID_ACCOUNT");
    }

    public InvalidAccountException(String message, String errorCode) {
        super(message, errorCode);
    }

    public InvalidAccountException(String message, Throwable cause) {
        super(message, "INVALID_ACCOUNT", cause);
    }
}