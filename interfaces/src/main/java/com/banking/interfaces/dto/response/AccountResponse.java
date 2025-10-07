package com.banking.interfaces.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * DTO de resposta para operações de conta bancária.
 * Representa os dados de uma conta que são expostos via API.
 */
@Schema(description = "Dados de uma conta bancária")
public class AccountResponse {

    @Schema(description = "Identificador único da conta", example = "a1b2c3d4-e5f6-4789-a012-b34567890123")
    private String id;

    @Schema(description = "Número da conta", example = "12345-6")
    private String accountNumber;

    @Schema(description = "Nome do titular da conta", example = "João Silva Santos")
    private String holderName;

    @Schema(description = "CPF do titular", example = "12345678901")
    private String holderCpf;

    @Schema(description = "Saldo atual da conta", example = "1250.75")
    private java.math.BigDecimal balance;

    @Schema(description = "Indica se a conta está ativa", example = "true")
    private Boolean isActive;

    @Schema(description = "Data e hora de criação da conta")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // Constructors
    public AccountResponse() {}

    public AccountResponse(String id, String accountNumber, String holderName, 
                          String holderCpf, java.math.BigDecimal balance, 
                          Boolean isActive, LocalDateTime createdAt) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.holderCpf = holderCpf;
        this.balance = balance;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

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

    public java.math.BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(java.math.BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AccountResponse{" +
                "id='" + id + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", holderName='" + holderName + '\'' +
                ", holderCpf='" + holderCpf + '\'' +
                ", balance=" + balance +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                '}';
    }
}