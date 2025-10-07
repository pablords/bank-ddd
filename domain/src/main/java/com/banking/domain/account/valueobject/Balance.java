package com.banking.domain.account.valueobject;

import com.banking.domain.shared.base.ValueObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Value Object que representa o saldo de uma conta bancária.
 * Garante precisão monetária e regras de negócio sobre valores.
 */
public class Balance extends ValueObject {

    private final BigDecimal amount;

    private Balance(BigDecimal amount) {
        requireNonNull(amount, "Balance amount cannot be null");
        this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        validate();
    }

    public static Balance of(BigDecimal amount) {
        return new Balance(amount);
    }

    public static Balance of(double amount) {
        return new Balance(BigDecimal.valueOf(amount));
    }

    public static Balance zero() {
        return new Balance(BigDecimal.ZERO);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Adiciona um valor ao saldo
     */
    public Balance add(Balance other) {
        requireNonNull(other, "Balance to add cannot be null");
        return new Balance(this.amount.add(other.amount));
    }

    /**
     * Subtrai um valor do saldo
     */
    public Balance subtract(Balance other) {
        requireNonNull(other, "Balance to subtract cannot be null");
        return new Balance(this.amount.subtract(other.amount));
    }

    /**
     * Verifica se o saldo é positivo
     */
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Verifica se o saldo é zero
     */
    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Verifica se o saldo é negativo
     */
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Verifica se há saldo suficiente para um valor
     */
    public boolean hasSufficientFunds(Balance required) {
        requireNonNull(required, "Required balance cannot be null");
        return this.amount.compareTo(required.amount) >= 0;
    }

    /**
     * Compara se este saldo é maior que outro
     */
    public boolean isGreaterThan(Balance other) {
        requireNonNull(other, "Balance to compare cannot be null");
        return this.amount.compareTo(other.amount) > 0;
    }

    /**
     * Compara se este saldo é menor que outro
     */
    public boolean isLessThan(Balance other) {
        requireNonNull(other, "Balance to compare cannot be null");
        return this.amount.compareTo(other.amount) < 0;
    }

    @Override
    protected void validate() {
        // Balance pode ser negativo em casos específicos (overdraft autorizado)
        // Validação adicional pode ser feita nas regras de negócio
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Balance balance = (Balance) obj;
        return amount.equals(balance.amount);
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }

    @Override
    public String toString() {
        return String.format("R$ %.2f", amount);
    }
}