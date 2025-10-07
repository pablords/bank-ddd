package com.banking.domain.transfer.valueobject;

import com.banking.domain.shared.base.ValueObject;

import java.util.UUID;

/**
 * Value Object que representa o identificador único de uma transferência.
 */
public class TransferId extends ValueObject {

    private final String value;

    private TransferId(String value) {
        requireNonBlank(value, "Transfer ID cannot be null or empty");
        this.value = value;
        validate();
    }

    public static TransferId of(String value) {
        return new TransferId(value);
    }

    public static TransferId generate() {
        return new TransferId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    protected void validate() {
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Transfer ID must be a valid UUID", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        TransferId that = (TransferId) obj;
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