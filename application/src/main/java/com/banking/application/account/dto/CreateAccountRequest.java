package com.banking.application.account.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO para requisição de criação de conta.
 */
public record CreateAccountRequest(
    
    @NotBlank(message = "Holder name is required")
    @Size(min = 2, max = 100, message = "Holder name must be between 2 and 100 characters")
    String holderName,
    
    @NotBlank(message = "CPF is required")
    @Size(min = 11, max = 14, message = "CPF must be valid")
    String holderCpf,
    
    @PositiveOrZero(message = "Initial balance must be positive or zero")
    BigDecimal initialBalance
) {

    public CreateAccountRequest {
        // Limpa o CPF de formatação
        if (holderCpf != null) {
            holderCpf = holderCpf.replaceAll("[^\\d]", "");
        }
        
        // Define saldo inicial padrão
        if (initialBalance == null) {
            initialBalance = BigDecimal.ZERO;
        }
    }

    /**
     * Cria uma requisição com saldo inicial zero
     */
    public static CreateAccountRequest of(String holderName, String holderCpf) {
        return new CreateAccountRequest(holderName, holderCpf, BigDecimal.ZERO);
    }

    /**
     * Cria uma requisição com saldo inicial específico
     */
    public static CreateAccountRequest of(String holderName, String holderCpf, BigDecimal initialBalance) {
        return new CreateAccountRequest(holderName, holderCpf, initialBalance);
    }
}