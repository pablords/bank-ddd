package com.banking.domain.account.event;

import com.banking.domain.shared.base.DomainEvent;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Domain Event disparado quando um débito é realizado em uma conta.
 */
public class AccountDebited extends DomainEvent {

    private final String accountId;
    private final BigDecimal debitAmount;
    private final BigDecimal newBalance;
    private final String reason;

    public AccountDebited(String accountId, BigDecimal debitAmount, 
                         BigDecimal newBalance, String reason) {
        super();
        this.accountId = accountId;
        this.debitAmount = debitAmount;
        this.newBalance = newBalance;
        this.reason = reason;
    }

    @Override
    public String getAggregateId() {
        return accountId;
    }

    @Override
    public String getAggregateType() {
        return "Account";
    }

    @Override
    public Object getEventData() {
        Map<String, Object> data = new HashMap<>();
        data.put("accountId", accountId);
        data.put("debitAmount", debitAmount);
        data.put("newBalance", newBalance);
        data.put("reason", reason);
        return data;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public String getReason() {
        return reason;
    }
}