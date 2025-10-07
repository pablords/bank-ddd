package com.banking.domain.transfer.event;

import com.banking.domain.shared.base.DomainEvent;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TransferFailed extends DomainEvent {
    private final String transferId;
    private final String fromAccountId;
    private final String toAccountId;
    private final BigDecimal amount;
    private final String reason;

    public TransferFailed(String transferId, String fromAccountId, String toAccountId, 
                         BigDecimal amount, String reason) {
        super();
        this.transferId = transferId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.reason = reason;
    }

    @Override public String getAggregateId() { return transferId; }
    @Override public String getAggregateType() { return "Transfer"; }
    
    @Override
    public Object getEventData() {
        Map<String, Object> data = new HashMap<>();
        data.put("transferId", transferId);
        data.put("fromAccountId", fromAccountId);
        data.put("toAccountId", toAccountId);
        data.put("amount", amount);
        data.put("reason", reason);
        return data;
    }

    // Getters
    public String getTransferId() { return transferId; }
    public String getFromAccountId() { return fromAccountId; }
    public String getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public String getReason() { return reason; }
}