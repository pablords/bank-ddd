package com.banking.domain.account.valueobject;

import com.banking.domain.shared.base.ValueObject;

import java.util.UUID;

/**
 * Value Object que representa o identificador único de uma conta.
 */
public class AccountId extends ValueObject {

    private final String value;

    private AccountId(String value) {
        requireNonBlank(value, "Account ID cannot be null or empty");
        this.value = value;
        validate();
    }

    public static AccountId of(String value) {
        return new AccountId(value);
    }

    public static AccountId generate() {
        return new AccountId(UUID.randomUUID().toString());
    }

    public String getValue() {
        return value;
    }

    @Override
    protected void validate() {
        try {
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Account ID must be a valid UUID", e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        AccountId accountId = (AccountId) obj;
        return value.equals(accountId.value);
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