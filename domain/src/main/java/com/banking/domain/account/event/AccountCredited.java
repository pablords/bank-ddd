package com.banking.domain.account.event;

import com.banking.domain.shared.base.DomainEvent;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Domain Event disparado quando um crédito é realizado em uma conta.
 */
public class AccountCredited extends DomainEvent {

    private final String accountId;
    private final BigDecimal creditAmount;
    private final BigDecimal newBalance;
    private final String reason;

    public AccountCredited(String accountId, BigDecimal creditAmount, 
                          BigDecimal newBalance, String reason) {
        super();
        this.accountId = accountId;
        this.creditAmount = creditAmount;
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
        data.put("creditAmount", creditAmount);
        data.put("newBalance", newBalance);
        data.put("reason", reason);
        return data;
    }

    public String getAccountId() {
        return accountId;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public String getReason() {
        return reason;
    }
}