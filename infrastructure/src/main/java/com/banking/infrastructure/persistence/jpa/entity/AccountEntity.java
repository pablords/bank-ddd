package com.banking.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade JPA para persistência de contas bancárias.
 * Representa a estrutura de dados no banco de dados.
 */
@Entity
@Table(name = "accounts", indexes = {
    @Index(name = "idx_account_number", columnList = "account_number", unique = true),
    @Index(name = "idx_holder_cpf", columnList = "holder_cpf", unique = true),
    @Index(name = "idx_account_active", columnList = "active")
})
public class AccountEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "account_number", length = 20, nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "holder_name", length = 255, nullable = false)
    private String holderName;

    @Column(name = "holder_cpf", length = 11, nullable = false, unique = true)
    private String holderCpf;

    @Column(name = "balance", precision = 15, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Construtor padrão para JPA
    public AccountEntity() {}

    public AccountEntity(String id, String accountNumber, String holderName, 
                        String holderCpf, BigDecimal balance, Boolean active) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.holderCpf = holderCpf;
        this.balance = balance;
        this.active = active;
        this.version = 0L;
    }

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public String getHolderCpf() { return holderCpf; }
    public void setHolderCpf(String holderCpf) { this.holderCpf = holderCpf; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

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
        AccountEntity that = (AccountEntity) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("AccountEntity{id='%s', accountNumber='%s', holderName='%s', balance=%s, active=%s}", 
            id, accountNumber, holderName, balance, active);
    }
}