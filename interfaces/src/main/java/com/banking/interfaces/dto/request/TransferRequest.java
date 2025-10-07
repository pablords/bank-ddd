package com.banking.interfaces.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * DTO para requisição de transferência bancária.
 * Inclui validações para garantir dados corretos e segurança da operação.
 */
@Schema(description = "Dados para realizar uma transferência bancária")
public class TransferRequest {

    @Schema(description = "Número da conta de origem", 
            example = "12345-6", 
            required = true)
    @NotBlank(message = "Número da conta de origem é obrigatório")
    private String fromAccountNumber;

    @Schema(description = "Número da conta de destino", 
            example = "23456-7", 
            required = true)
    @NotBlank(message = "Número da conta de destino é obrigatório")
    private String toAccountNumber;

    @Schema(description = "Valor da transferência", 
            example = "150.75", 
            required = true,
            minimum = "0.01")
    @NotNull(message = "Valor da transferência é obrigatório")
    @Positive(message = "Valor da transferência deve ser positivo")
    private java.math.BigDecimal amount;

    @Schema(description = "Descrição opcional da transferência", 
            example = "Pagamento de serviços",
            maxLength = 255)
    @Size(max = 255, message = "Descrição não pode exceder 255 caracteres")
    private String description;

    @Schema(description = "Chave de idempotência para evitar transferências duplicadas", 
            example = "transfer-uuid-123",
            required = true)
    @NotBlank(message = "Chave de idempotência é obrigatória")
    @Size(min = 10, max = 64, message = "Chave de idempotência deve ter entre 10 e 64 caracteres")
    private String idempotencyKey;

    // Constructors
    public TransferRequest() {}

    public TransferRequest(String fromAccountNumber, String toAccountNumber, 
                          java.math.BigDecimal amount, String description, String idempotencyKey) {
        this.fromAccountNumber = fromAccountNumber;
        this.toAccountNumber = toAccountNumber;
        this.amount = amount;
        this.description = description;
        this.idempotencyKey = idempotencyKey;
    }

    // Getters and Setters
    public String getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(String fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public String getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(String toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
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

    @Override
    public String toString() {
        return "TransferRequest{" +
                "fromAccountNumber='" + fromAccountNumber + '\'' +
                ", toAccountNumber='" + toAccountNumber + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", idempotencyKey='" + idempotencyKey + '\'' +
                '}';
    }
}