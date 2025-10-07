package com.banking.domain.account.valueobject;

import com.banking.domain.shared.base.ValueObject;

import java.util.regex.Pattern;

/**
 * Value Object que representa um CPF válido.
 * Inclui validação de formato e dígitos verificadores.
 */
public class Cpf extends ValueObject {

    private static final Pattern CPF_PATTERN = Pattern.compile("\\d{11}");
    private final String value;

    private Cpf(String value) {
        requireNonBlank(value, "CPF cannot be null or empty");
        this.value = cleanCpf(value);
        validate();
    }

    public static Cpf of(String value) {
        return new Cpf(value);
    }

    public String getValue() {
        return value;
    }

    /**
     * Retorna o CPF formatado (xxx.xxx.xxx-xx)
     */
    public String getFormattedValue() {
        return String.format("%s.%s.%s-%s", 
            value.substring(0, 3),
            value.substring(3, 6),
            value.substring(6, 9),
            value.substring(9, 11));
    }

    /**
     * Remove formatação do CPF
     */
    private String cleanCpf(String cpf) {
        return cpf.replaceAll("[^\\d]", "");
    }

    @Override
    protected void validate() {
        if (!CPF_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("CPF must have exactly 11 digits");
        }

        if (isSequentialDigits(value)) {
            throw new IllegalArgumentException("CPF cannot be sequential digits");
        }

        if (!isValidCpf(value)) {
            throw new IllegalArgumentException("Invalid CPF check digits");
        }
    }

    /**
     * Verifica se o CPF tem todos os dígitos iguais
     */
    private boolean isSequentialDigits(String cpf) {
        return cpf.chars().distinct().count() == 1;
    }

    /**
     * Valida os dígitos verificadores do CPF
     */
    private boolean isValidCpf(String cpf) {
        // Cálculo do primeiro dígito verificador
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int firstDigit = 11 - (sum % 11);
        if (firstDigit >= 10) firstDigit = 0;

        // Cálculo do segundo dígito verificador
        sum = 0;
        for (int i = 0; i < 10; i++) {
            sum += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        int secondDigit = 11 - (sum % 11);
        if (secondDigit >= 10) secondDigit = 0;

        // Verifica se os dígitos calculados correspondem aos fornecidos
        return Character.getNumericValue(cpf.charAt(9)) == firstDigit &&
               Character.getNumericValue(cpf.charAt(10)) == secondDigit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Cpf cpf = (Cpf) obj;
        return value.equals(cpf.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return getFormattedValue();
    }
}