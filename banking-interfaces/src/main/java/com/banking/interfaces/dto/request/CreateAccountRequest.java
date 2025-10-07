package com.banking.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de criação de conta bancária.
 * Contém validações para garantir integridade dos dados de entrada.
 */
@Schema(description = "Dados para criação de nova conta bancária")
public class CreateAccountRequest {

    @Schema(description = "Nome completo do titular da conta", 
            example = "João Silva Santos", 
            required = true)
    @NotBlank(message = "Nome do titular é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String holderName;

    @Schema(description = "CPF do titular da conta (apenas números)", 
            example = "12345678901", 
            required = true,
            pattern = "^\\d{11}$")
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "^\\d{11}$", message = "CPF deve conter exatamente 11 dígitos numéricos")
    private String holderCpf;

    @Schema(description = "Valor inicial de depósito na conta", 
            example = "1000.00",
            minimum = "0")
    private java.math.BigDecimal initialBalance = java.math.BigDecimal.ZERO;

    // Constructors
    public CreateAccountRequest() {}

    public CreateAccountRequest(String holderName, String holderCpf, java.math.BigDecimal initialBalance) {
        this.holderName = holderName;
        this.holderCpf = holderCpf;
        this.initialBalance = initialBalance;
    }

    // Getters and Setters
    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getHolderCpf() {
        return holderCpf;
    }

    public void setHolderCpf(String holderCpf) {
        this.holderCpf = holderCpf;
    }

    public java.math.BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(java.math.BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    @Override
    public String toString() {
        return "CreateAccountRequest{" +
                "holderName='" + holderName + '\'' +
                ", holderCpf='" + holderCpf + '\'' +
                ", initialBalance=" + initialBalance +
                '}';
    }
}