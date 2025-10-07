package com.banking.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade JPA para persistência de transferências.
 * Representa a estrutura de dados das transferências no banco de dados.
 */
@Entity
@Table(name = "transfers", indexes = {
    @Index(name = "idx_idempotency_key", columnList = "idempotency_key", unique = true),
    @Index(name = "idx_from_account", columnList = "from_account_id"),
    @Index(name = "idx_to_account", columnList = "to_account_id"),
    @Index(name = "idx_transfer_status", columnList = "status"),
    @Index(name = "idx_transfer_created", columnList = "created_at")
})
public class TransferEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "from_account_id", length = 36, nullable = false)
    private String fromAccountId;

    @Column(name = "to_account_id", length = 36, nullable = false)
    private String toAccountId;

    @Column(name = "amount", precision = 15, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private TransferStatusEnum status;

    @Column(name = "idempotency_key", length = 255, nullable = false, unique = true)
    private String idempotencyKey;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "failure_reason", length = 1000)
    private String failureReason;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Enum para status da transferência
    public enum TransferStatusEnum {
        PENDING, PROCESSING, COMPLETED, FAILED, CANCELLED
    }

    // Construtor padrão para JPA
    protected TransferEntity() {}

    public TransferEntity(String id, String fromAccountId, String toAccountId, 
                         BigDecimal amount, TransferStatusEnum status, String idempotencyKey, 
                         String description) {
        this.id = id;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = status;
        this.idempotencyKey = idempotencyKey;
        this.description = description;
        this.version = 0L;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(String fromAccountId) { this.fromAccountId = fromAccountId; }

    public String getToAccountId() { return toAccountId; }
    public void setToAccountId(String toAccountId) { this.toAccountId = toAccountId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public TransferStatusEnum getStatus() { return status; }
    public void setStatus(TransferStatusEnum status) { this.status = status; }

    public String getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TransferEntity that = (TransferEntity) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("TransferEntity{id='%s', from='%s', to='%s', amount=%s, status=%s}", 
            id, fromAccountId, toAccountId, amount, status);
    }
}