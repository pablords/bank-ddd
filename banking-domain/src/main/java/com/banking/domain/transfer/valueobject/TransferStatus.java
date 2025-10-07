package com.banking.domain.transfer.valueobject;

/**
 * Enum que representa os possíveis status de uma transferência.
 */
public enum TransferStatus {
    
    PENDING("Pendente - aguardando processamento"),
    PROCESSING("Em processamento"),
    COMPLETED("Concluída com sucesso"),
    FAILED("Falhou durante o processamento"),
    CANCELLED("Cancelada");

    private final String description;

    TransferStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public boolean isPending() {
        return this == PENDING;
    }

    public boolean isProcessing() {
        return this == PROCESSING;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public boolean isFailed() {
        return this == FAILED;
    }

    public boolean isCancelled() {
        return this == CANCELLED;
    }

    public boolean isFinal() {
        return this == COMPLETED || this == FAILED || this == CANCELLED;
    }

    public boolean canTransitionTo(TransferStatus newStatus) {
        return switch (this) {
            case PENDING -> newStatus == PROCESSING || newStatus == CANCELLED;
            case PROCESSING -> newStatus == COMPLETED || newStatus == FAILED;
            case COMPLETED, FAILED, CANCELLED -> false;
        };
    }
}