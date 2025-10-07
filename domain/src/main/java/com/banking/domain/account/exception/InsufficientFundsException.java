package com.banking.domain.account.exception;

import com.banking.domain.shared.exception.DomainException;

/**
 * Exceção lançada quando uma operação é tentada com saldo insuficiente.
 */
public class InsufficientFundsException extends DomainException {

    public InsufficientFundsException(String message) {
        super(message, "INSUFFICIENT_FUNDS");
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, "INSUFFICIENT_FUNDS", cause);
    }
}