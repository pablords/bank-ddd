package com.banking.domain.transfer.valueobject;

import com.banking.domain.shared.base.ValueObject;

import java.util.UUID;

/**
 * Value Object que representa uma chave de idempotência para transferências.
 * Garante que a mesma operação não seja executada múltiplas vezes.
 */
public class IdempotencyKey extends ValueObject {

    private final String value;

    private IdempotencyKey(String value) {
        requireNonBlank(value, "Idempotency key cannot be null or empty");
        this.value = value.trim();
        validate();
    }

    public static IdempotencyKey of(String value) {
        return new IdempotencyKey(value);
    }

    public static IdempotencyKey generate() {
        return new IdempotencyKey(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    protected void validate() {
        if (value.length() < 8) {
            throw new IllegalArgumentException("Idempotency key must have at least 8 characters");
        }
        
        if (value.length() > 255) {
            throw new IllegalArgumentException("Idempotency key cannot exceed 255 characters");
        }

        if (!value.matches("^[a-zA-Z0-9\\-_]+$")) {
            throw new IllegalArgumentException("Idempotency key must contain only alphanumeric characters, hyphens, and underscores");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        IdempotencyKey that = (IdempotencyKey) obj;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return value;
    }
}