package com.banking.application.account.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para resposta de consulta de conta.
 */
public record AccountResponse(
    String id,
    String accountNumber,
    String holderName,
    String holderCpf,
    BigDecimal balance,
    boolean active,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

    /**
     * Retorna uma versão formatada do CPF
     */
    public String getFormattedCpf() {
        if (holderCpf == null || holderCpf.length() != 11) {
            return holderCpf;
        }
        return String.format("%s.%s.%s-%s", 
            holderCpf.substring(0, 3),
            holderCpf.substring(3, 6),
            holderCpf.substring(6, 9),
            holderCpf.substring(9, 11));
    }

    /**
     * Retorna uma versão formatada do número da conta
     */
    public String getFormattedAccountNumber() {
        if (accountNumber == null || accountNumber.length() != 8) {
            return accountNumber;
        }
        return String.format("%s-%s", 
            accountNumber.substring(0, 7),
            accountNumber.substring(7));
    }

    /**
     * Retorna uma versão formatada do saldo
     */
    public String getFormattedBalance() {
        return String.format("R$ %.2f", balance);
    }

    /**
     * Verifica se a conta tem saldo positivo
     */
    public boolean hasPositiveBalance() {
        return balance != null && balance.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Factory method para criar a partir de entidade de domínio
     */
    public static AccountResponse from(com.banking.domain.account.entity.Account account) {
        return new AccountResponse(
            account.getId().getValue(),
            account.getAccountNumber().getValue(),
            account.getHolderName().getValue(),
            account.getHolderCpf().getValue(),
            account.getBalance().getAmount(),
            account.isActive(),
            account.getCreatedAt(),
            account.getUpdatedAt()
        );
    }
}