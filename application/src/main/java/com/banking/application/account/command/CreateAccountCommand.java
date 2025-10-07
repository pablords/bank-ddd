package com.banking.application.account.command;

import com.banking.application.account.dto.CreateAccountRequest;
import com.banking.application.shared.base.Command;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Command para criação de nova conta bancária.
 */
public record CreateAccountCommand(
    @NotNull(message = "Request cannot be null")
    @Valid
    CreateAccountRequest request
) implements Command {

    /**
     * Factory method para criar command a partir de request
     */
    public static CreateAccountCommand from(CreateAccountRequest request) {
        return new CreateAccountCommand(request);
    }

    /**
     * Retorna o nome do titular
     */
    public String getHolderName() {
        return request.holderName();
    }

    /**
     * Retorna o CPF do titular
     */
    public String getHolderCpf() {
        return request.holderCpf();
    }

    /**
     * Retorna o saldo inicial
     */
    public java.math.BigDecimal getInitialBalance() {
        return request.initialBalance();
    }

    @Override
    public String toString() {
        return String.format("CreateAccountCommand{holderName='%s', holderCpf='***', initialBalance=%s}", 
            request.holderName(), request.initialBalance());
    }
}