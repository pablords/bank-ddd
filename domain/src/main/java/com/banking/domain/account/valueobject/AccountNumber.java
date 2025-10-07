package com.banking.domain.account.valueobject;

import com.banking.domain.shared.base.ValueObject;

import java.util.Random;

/**
 * Value Object que representa um número de conta bancária.
 * Gera números únicos e válidos seguindo padrões bancários.
 */
public class AccountNumber extends ValueObject {

    private static final int ACCOUNT_NUMBER_LENGTH = 8;
    private static final Random RANDOM = new Random();
    
    private final String value;

    private AccountNumber(String value) {
        requireNonBlank(value, "Account number cannot be null or empty");
        this.value = value;
        validate();
    }

    public static AccountNumber of(String value) {
        return new AccountNumber(value);
    }

    public static AccountNumber generate() {
        StringBuilder accountNumber = new StringBuilder();
        
        // Gera 7 dígitos aleatórios
        for (int i = 0; i < ACCOUNT_NUMBER_LENGTH - 1; i++) {
            accountNumber.append(RANDOM.nextInt(10));
        }
        
        // Calcula dígito verificador
        int checkDigit = calculateCheckDigit(accountNumber.toString());
        accountNumber.append(checkDigit);
        
        return new AccountNumber(accountNumber.toString());
    }

    public String getValue() {
        return value;
    }

    /**
     * Retorna o número da conta formatado (xxxxxxx-x)
     */
    public String getFormattedValue() {
        return String.format("%s-%s", 
            value.substring(0, ACCOUNT_NUMBER_LENGTH - 1),
            value.substring(ACCOUNT_NUMBER_LENGTH - 1));
    }

    /**
     * Calcula dígito verificador usando algoritmo módulo 11
     */
    private static int calculateCheckDigit(String accountNumber) {
        int sum = 0;
        int weight = 2;
        
        // Soma os produtos dos dígitos pelos pesos de 2 a 9
        for (int i = accountNumber.length() - 1; i >= 0; i--) {
            sum += Character.getNumericValue(accountNumber.charAt(i)) * weight;
            weight++;
            if (weight > 9) weight = 2;
        }
        
        int remainder = sum % 11;
        return remainder < 2 ? 0 : 11 - remainder;
    }

    /**
     * Valida o dígito verificador do número da conta
     */
    private boolean isValidCheckDigit() {
        if (value.length() != ACCOUNT_NUMBER_LENGTH) {
            return false;
        }
        
        String baseNumber = value.substring(0, ACCOUNT_NUMBER_LENGTH - 1);
        int providedCheckDigit = Character.getNumericValue(value.charAt(ACCOUNT_NUMBER_LENGTH - 1));
        int calculatedCheckDigit = calculateCheckDigit(baseNumber);
        
        return providedCheckDigit == calculatedCheckDigit;
    }

    @Override
    protected void validate() {
        if (value.length() != ACCOUNT_NUMBER_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Account number must have exactly %d digits", ACCOUNT_NUMBER_LENGTH));
        }

        if (!value.matches("\\d+")) {
            throw new IllegalArgumentException("Account number must contain only digits");
        }

        if (!isValidCheckDigit()) {
            throw new IllegalArgumentException("Invalid account number check digit");
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        AccountNumber that = (AccountNumber) obj;
        return value.equals(that.value);
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