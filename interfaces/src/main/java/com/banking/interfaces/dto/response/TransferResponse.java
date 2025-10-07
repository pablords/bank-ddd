package com.banking.interfaces.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * DTO de resposta para operações de transferência bancária.
 * Representa os dados de uma transferência que são expostos via API.
 */
@Schema(description = "Dados de uma transferência bancária")
public class TransferResponse {

    @Schema(description = "Identificador único da transferência", example = "t1b2c3d4-e5f6-4789-a012-b34567890123")
    private String id;

    @Schema(description = "ID da conta de origem", example = "a1b2c3d4-e5f6-4789-a012-b34567890123")
    private String fromAccountId;

    @Schema(description = "Número da conta de origem", example = "12345-6")
    private String fromAccountNumber;

    @Schema(description = "Nome do titular da conta de origem", example = "João Silva")
    private String fromAccountHolderName;

    @Schema(description = "ID da conta de destino", example = "b2c3d4e5-f6a7-4890-b123-c45678901234")
    private String toAccountId;

    @Schema(description = "Número da conta de destino", example = "23456-7")
    private String toAccountNumber;

    @Schema(description = "Nome do titular da conta de destino", example = "Maria Santos")
    private String toAccountHolderName;

    @Schema(description = "Valor da transferência", example = "150.75")
    private java.math.BigDecimal amount;

    @Schema(description = "Descrição da transferência", example = "Pagamento de serviços")
    private String description;

    @Schema(description = "Chave de idempotência", example = "transfer-uuid-123")
    private String idempotencyKey;

    @Schema(description = "Status da transferência", example = "COMPLETED")
    private String status;

    @Schema(description = "Data e hora de criação da transferência")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    // Constructors
    public TransferResponse() {}

    public TransferResponse(String id, String fromAccountId, String fromAccountNumber, 
                           String fromAccountHolderName, String toAccountId, String toAccountNumber,
                           String toAccountHolderName, java.math.BigDecimal amount, String description,
                           String idempotencyKey, String status, LocalDateTime createdAt) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.fromAccountNumber = fromAccountNumber;
        this.fromAccountHolderName = fromAccountHolderName;
        this.toAccountId = toAccountId;
        this.toAccountNumber = toAccountNumber;
        this.toAccountHolderName = toAccountHolderName;
        this.amount = amount;
        this.description = description;
        this.idempotencyKey = idempotencyKey;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(String fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getFromAccountHolderName() {
        return fromAccountHolderName;
    }

    public void setFromAccountHolderName(String fromAccountHolderName) {
        this.fromAccountHolderName = fromAccountHolderName;
    }

    public String getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(String toAccountId) {
        this.toAccountId = toAccountId;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public String getToAccountHolderName() {
        return toAccountHolderName;
    }

    public void setToAccountHolderName(String toAccountHolderName) {
        this.toAccountHolderName = toAccountHolderName;
    }

    public java.math.BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(java.math.BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TransferResponse{" +
                "id='" + id + '\'' +
                ", fromAccountId='" + fromAccountId + '\'' +
                ", fromAccountNumber='" + fromAccountNumber + '\'' +
                ", fromAccountHolderName='" + fromAccountHolderName + '\'' +
                ", toAccountId='" + toAccountId + '\'' +
                ", toAccountNumber='" + toAccountNumber + '\'' +
                ", toAccountHolderName='" + toAccountHolderName + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", idempotencyKey='" + idempotencyKey + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}