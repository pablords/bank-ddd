package com.banking.application.transfer.dto;

import com.banking.domain.transfer.valueobject.TransferStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para resposta de transferência.
 */
public record TransferResponse(
    String id,
    String fromAccountId,
    String toAccountId,
    BigDecimal amount,
    String formattedAmount,
    TransferStatus status,
    String description,
    String idempotencyKey,
    String failureReason,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    public TransferResponse {
        if (amount != null && formattedAmount == null) {
            formattedAmount = String.format("R$ %.2f", amount);
        }
    }

    /**
     * Verifica se a transferência foi concluída com sucesso
     */
    public boolean isCompleted() {
        return status == TransferStatus.COMPLETED;
    }

    /**
     * Verifica se a transferência falhou
     */
    public boolean isFailed() {
        return status == TransferStatus.FAILED;
    }

    /**
     * Verifica se a transferência está pendente
     */
    public boolean isPending() {
        return status == TransferStatus.PENDING;
    }

    /**
     * Verifica se a transferência está em processamento
     */
    public boolean isProcessing() {
        return status == TransferStatus.PROCESSING;
    }

    /**
     * Factory method para criar a partir de entidade de domínio
     */
    public static TransferResponse from(com.banking.domain.transfer.entity.Transfer transfer) {
        return new TransferResponse(
            transfer.getId().getValue(),
            transfer.getFromAccountId().getValue(),
            transfer.getToAccountId().getValue(),
            transfer.getAmount().getValue(),
            String.format("R$ %.2f", transfer.getAmount().getValue()),
            transfer.getStatus(),
            transfer.getDescription(),
            transfer.getIdempotencyKey().getValue(),
            transfer.getFailureReason(),
            transfer.getCreatedAt(),
            transfer.getUpdatedAt()
        );
    }
}