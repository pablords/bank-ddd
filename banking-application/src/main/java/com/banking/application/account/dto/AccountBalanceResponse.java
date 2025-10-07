package com.banking.application.account.dto;

import java.math.BigDecimal;

/**
 * DTO para resposta de consulta de saldo.
 */
public record AccountBalanceResponse(
    String accountId,
    String accountNumber,
    BigDecimal balance,
    String formattedBalance,
    boolean hasPositiveBalance
) {

    public AccountBalanceResponse(String accountId, String accountNumber, BigDecimal balance) {
        this(
            accountId,
            accountNumber,
            balance,
            String.format("R$ %.2f", balance),
            balance.compareTo(BigDecimal.ZERO) > 0
        );
    }

    /**
     * Factory method para criar a partir de entidade de domínio
     */
    public static AccountBalanceResponse from(com.banking.domain.account.entity.Account account) {
        return new AccountBalanceResponse(
            account.getId().getValue(),
            account.getAccountNumber().getValue(),
            account.getBalance().getAmount()
        );
    }
}