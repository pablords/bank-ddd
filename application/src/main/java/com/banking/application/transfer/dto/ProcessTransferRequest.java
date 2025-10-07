package com.banking.application.transfer.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * DTO para requisição de processamento de transferência.
 */
public record ProcessTransferRequest(
    
    @NotBlank(message = "From account ID is required")
    String fromAccountId,
    
    @NotBlank(message = "To account ID is required") 
    String toAccountId,
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be at least 0.01")
    @DecimalMax(value = "1000000.00", message = "Amount cannot exceed 1,000,000.00")
    BigDecimal amount,
    
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    String description,
    
    @Size(max = 255, message = "Idempotency key cannot exceed 255 characters")
    String idempotencyKey
) {

    public ProcessTransferRequest {
        // Valida que as contas são diferentes
        if (fromAccountId != null && fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        
        // Gera chave de idempotência se não fornecida
        if (idempotencyKey == null || idempotencyKey.trim().isEmpty()) {
            idempotencyKey = java.util.UUID.randomUUID().toString();
        }
        
        // Define descrição padrão se não fornecida
        if (description == null || description.trim().isEmpty()) {
            description = "Transfer between accounts";
        }
    }

    /**
     * Factory method para criar transferência simples
     */
    public static ProcessTransferRequest of(String fromAccountId, String toAccountId, BigDecimal amount) {
        return new ProcessTransferRequest(fromAccountId, toAccountId, amount, null, null);
    }

    /**
     * Factory method para criar transferência com descrição
     */
    public static ProcessTransferRequest of(String fromAccountId, String toAccountId, 
                                          BigDecimal amount, String description) {
        return new ProcessTransferRequest(fromAccountId, toAccountId, amount, description, null);
    }

    /**
     * Factory method para criar transferência com chave de idempotência
     */
    public static ProcessTransferRequest of(String fromAccountId, String toAccountId, 
                                          BigDecimal amount, String description, String idempotencyKey) {
        return new ProcessTransferRequest(fromAccountId, toAccountId, amount, description, idempotencyKey);
    }
}