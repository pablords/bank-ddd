package com.banking.domain.transfer.valueobject;

import com.banking.domain.shared.base.ValueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Value Object que representa o valor monetário de uma transferência.
 */
public class Amount extends ValueObject {

    private static final BigDecimal MINIMUM_AMOUNT = new BigDecimal("0.01");
    private static final BigDecimal MAXIMUM_AMOUNT = new BigDecimal("1000000.00");
    
    private final BigDecimal value;

    private Amount(BigDecimal value) {
        requireNonNull(value, "Amount cannot be null");
        this.value = value.setScale(2, RoundingMode.HALF_UP);
        validate();
    }

    public static Amount of(BigDecimal value) {
        return new Amount(value);
    }

    public static Amount of(double value) {
        return new Amount(BigDecimal.valueOf(value));
    }

    public BigDecimal getValue() {
        return value;
    }

    public boolean isPositive() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isGreaterThan(Amount other) {
        requireNonNull(other, "Amount to compare cannot be null");
        return this.value.compareTo(other.value) > 0;
    }

    public boolean isLessThan(Amount other) {
        requireNonNull(other, "Amount to compare cannot be null");
        return this.value.compareTo(other.value) < 0;
    }

    @Override
    protected void validate() {
        requirePositive(value, "Transfer amount must be positive");
        
        if (value.compareTo(MINIMUM_AMOUNT) < 0) {
            throw new IllegalArgumentException(
                String.format("Transfer amount must be at least %s", MINIMUM_AMOUNT));
        }
        
        if (value.compareTo(MAXIMUM_AMOUNT) > 0) {
            throw new IllegalArgumentException(
                String.format("Transfer amount cannot exceed %s", MAXIMUM_AMOUNT));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Amount amount = (Amount) obj;
        return value.equals(amount.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return String.format("R$ %.2f", value);
    }
}