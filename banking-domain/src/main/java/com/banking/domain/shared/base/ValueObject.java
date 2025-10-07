package com.banking.domain.shared.base;

import java.util.Objects;

/**
 * Classe base abstrata para todos os Value Objects do domínio.
 * Value Objects são imutáveis e definidos por seus valores, não por identidade.
 */
public abstract class ValueObject {

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();

    /**
     * Valida se o value object está em um estado válido.
     * Deve ser chamado no construtor de classes filhas.
     */
    protected abstract void validate();

    /**
     * Helper method para validar argumentos não nulos
     */
    protected void requireNonNull(Object value, String message) {
        Objects.requireNonNull(value, message);
    }

    /**
     * Helper method para validar strings não vazias
     */
    protected void requireNonBlank(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Helper method para validar números positivos
     */
    protected void requirePositive(Number value, String message) {
        if (value == null || value.doubleValue() <= 0) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Helper method para validar números não negativos
     */
    protected void requireNonNegative(Number value, String message) {
        if (value == null || value.doubleValue() < 0) {
            throw new IllegalArgumentException(message);
        }
    }
}