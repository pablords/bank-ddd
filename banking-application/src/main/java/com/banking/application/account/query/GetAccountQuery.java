package com.banking.application.account.query;

import com.banking.application.account.dto.AccountResponse;
import com.banking.application.shared.base.Query;
import jakarta.validation.constraints.NotBlank;

/**
 * Query para consultar uma conta por ID.
 */
public record GetAccountQuery(
    @NotBlank(message = "Account ID is required")
    String accountId
) implements Query<AccountResponse> {

    public static GetAccountQuery of(String accountId) {
        return new GetAccountQuery(accountId);
    }

    @Override
    public String getCacheKey() {
        return "account_" + accountId;
    }

    @Override
    public int getCacheTtlSeconds() {
        return 600; // 10 minutos
    }

    @Override
    public String toString() {
        return String.format("GetAccountQuery{accountId='%s'}", accountId);
    }
}