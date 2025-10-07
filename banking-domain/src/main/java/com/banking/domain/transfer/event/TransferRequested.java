package com.banking.domain.transfer.event;

import com.banking.domain.shared.base.DomainEvent;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TransferRequested extends DomainEvent {
    private final String transferId;
    private final String fromAccountId;
    private final String toAccountId;
    private final BigDecimal amount;
    private final String idempotencyKey;
    private final String description;

    public TransferRequested(String transferId, String fromAccountId, String toAccountId, 
                           BigDecimal amount, String idempotencyKey, String description) {
        super();
        this.transferId = transferId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.idempotencyKey = idempotencyKey;
        this.description = description;
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
        data.put("idempotencyKey", idempotencyKey);
        data.put("description", description);
        return data;
    }

    // Getters
    public String getTransferId() { return transferId; }
    public String getFromAccountId() { return fromAccountId; }
    public String getToAccountId() { return toAccountId; }
    public BigDecimal getAmount() { return amount; }
    public String getIdempotencyKey() { return idempotencyKey; }
    public String getDescription() { return description; }
}