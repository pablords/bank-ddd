package com.banking.domain.account.valueobject;

import com.banking.domain.shared.base.ValueObject;

/**
 * Value Object que representa o nome do titular de uma conta.
 */
public class HolderName extends ValueObject {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 100;
    
    private final String value;

    private HolderName(String value) {
        requireNonBlank(value, "Holder name cannot be null or empty");
        this.value = value.trim();
        validate();
    }

    public static HolderName of(String value) {
        return new HolderName(value);
    }

    public String getValue() {
        return value;
    }

    /**
     * Retorna o nome formatado (primeira letra maiúscula)
     */
    public String getFormattedValue() {
        String[] words = value.toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) {
                result.append(" ");
            }
            if (!words[i].isEmpty()) {
                result.append(words[i].substring(0, 1).toUpperCase())
                      .append(words[i].substring(1));
            }
        }
        
        return result.toString();
    }

    @Override
    protected void validate() {
        if (value.length() < MIN_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Holder name must have at least %d characters", MIN_LENGTH));
        }

        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Holder name cannot exceed %d characters", MAX_LENGTH));
        }

        if (!value.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            throw new IllegalArgumentException("Holder name must contain only letters and spaces");
        }

        if (value.trim().split("\\s+").length < 2) {
            throw new IllegalArgumentException("Holder name must contain at least first and last name");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        HolderName that = (HolderName) obj;
        return value.equalsIgnoreCase(that.value);
    }

    @Override
    public int hashCode() {
        return value.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return getFormattedValue();
    }
}