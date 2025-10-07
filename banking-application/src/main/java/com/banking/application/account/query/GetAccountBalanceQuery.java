package com.banking.application.account.query;

import com.banking.application.account.dto.AccountBalanceResponse;
import com.banking.application.shared.base.Query;
import jakarta.validation.constraints.NotBlank;

/**
 * Query para consultar o saldo de uma conta.
 */
public record GetAccountBalanceQuery(
    @NotBlank(message = "Account ID is required")
    String accountId
) implements Query<AccountBalanceResponse> {

    public static GetAccountBalanceQuery of(String accountId) {
        return new GetAccountBalanceQuery(accountId);
    }

    @Override
    public String getCacheKey() {
        return "account_balance_" + accountId;
    }

    @Override
    public int getCacheTtlSeconds() {
        return 60; // 1 minuto (saldo muda frequentemente)
    }

    @Override
    public String toString() {
        return String.format("GetAccountBalanceQuery{accountId='%s'}", accountId);
    }
}