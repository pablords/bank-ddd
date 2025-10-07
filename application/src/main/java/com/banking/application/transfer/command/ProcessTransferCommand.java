package com.banking.application.transfer.command;

import com.banking.application.shared.base.Command;
import com.banking.application.transfer.dto.ProcessTransferRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Command para processar uma transferência bancária.
 */
public record ProcessTransferCommand(
    @NotNull(message = "Request cannot be null")
    @Valid
    ProcessTransferRequest request
) implements Command {

    /**
     * Factory method para criar command a partir de request
     */
    public static ProcessTransferCommand from(ProcessTransferRequest request) {
        return new ProcessTransferCommand(request);
    }

    /**
     * Retorna o ID da conta de origem
     */
    public String getFromAccountId() {
        return request.fromAccountId();
    }

    /**
     * Retorna o ID da conta de destino
     */
    public String getToAccountId() {
        return request.toAccountId();
    }

    /**
     * Retorna o valor da transferência
     */
    public java.math.BigDecimal getAmount() {
        return request.amount();
    }

    /**
     * Retorna a descrição da transferência
     */
    public String getDescription() {
        return request.description();
    }

    /**
     * Retorna a chave de idempotência
     */
    public String getIdempotencyKey() {
        return request.idempotencyKey();
    }

    @Override
    public String toString() {
        return String.format("ProcessTransferCommand{from='%s', to='%s', amount=%s, key='%s'}", 
            request.fromAccountId(), request.toAccountId(), request.amount(), request.idempotencyKey());
    }
}