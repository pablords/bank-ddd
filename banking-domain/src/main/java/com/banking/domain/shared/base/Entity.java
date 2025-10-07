package com.banking.domain.shared.base;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Classe base abstrata para todas as entidades do domínio.
 * Implementa conceitos fundamentais de identidade e auditoria.
 */
public abstract class Entity<ID> {
    
    protected ID id;
    protected LocalDateTime createdAt;
    protected LocalDateTime updatedAt;
    protected Long version;

    protected Entity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.version = 0L;
    }

    protected Entity(ID id) {
        this();
        this.id = Objects.requireNonNull(id, "ID cannot be null");
    }

    public ID getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Long getVersion() {
        return version;
    }

    protected void markAsUpdated() {
        this.updatedAt = LocalDateTime.now();
        this.version++;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Entity<?> entity = (Entity<?>) obj;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("%s{id=%s, version=%d}", 
            getClass().getSimpleName(), id, version);
    }
}