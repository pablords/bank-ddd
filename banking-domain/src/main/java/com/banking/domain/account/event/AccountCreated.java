package com.banking.domain.account.event;

import com.banking.domain.shared.base.DomainEvent;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Domain Event disparado quando uma conta é criada.
 */
public class AccountCreated extends DomainEvent {

    private final String accountId;
    private final String accountNumber;
    private final String holderName;
    private final String holderCpf;
    private final BigDecimal initialBalance;

    public AccountCreated(String accountId, String accountNumber, String holderName, 
                         String holderCpf, BigDecimal initialBalance) {
        super();
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.holderCpf = holderCpf;
        this.initialBalance = initialBalance;
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
        data.put("accountNumber", accountNumber);
        data.put("holderName", holderName);
        data.put("holderCpf", holderCpf);
        data.put("initialBalance", initialBalance);
        return data;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getHolderCpf() {
        return holderCpf;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }
}