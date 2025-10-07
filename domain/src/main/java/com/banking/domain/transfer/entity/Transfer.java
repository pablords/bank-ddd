package com.banking.domain.transfer.entity;

import com.banking.domain.account.valueobject.AccountId;
import com.banking.domain.shared.base.AggregateRoot;
import com.banking.domain.transfer.event.TransferCompleted;
import com.banking.domain.transfer.event.TransferFailed;
import com.banking.domain.transfer.event.TransferRequested;
import com.banking.domain.transfer.valueobject.*;

/**
 * Aggregate Root que representa uma transferência bancária.
 */
public class Transfer extends AggregateRoot<TransferId> {

    private AccountId fromAccountId;
    private AccountId toAccountId;
    private Amount amount;
    private TransferStatus status;
    private IdempotencyKey idempotencyKey;
    private String description;
    private String failureReason;

    private Transfer(TransferId id, AccountId fromAccountId, AccountId toAccountId, 
                    Amount amount, IdempotencyKey idempotencyKey, String description) {
        super(id);
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amount = amount;
        this.status = TransferStatus.PENDING;
        this.idempotencyKey = idempotencyKey;
        this.description = description;
        
        validateInvariants();
        addDomainEvent(new TransferRequested(
            id.getValue(),
            fromAccountId.getValue(),
            toAccountId.getValue(),
            amount.getValue(),
            idempotencyKey.getValue(),
            description
        ));
    }

    public static Transfer create(AccountId fromAccountId, AccountId toAccountId, 
                                 Amount amount, IdempotencyKey idempotencyKey, String description) {
        TransferId id = TransferId.generate();
        return new Transfer(id, fromAccountId, toAccountId, amount, idempotencyKey, description);
    }

    // Package-private constructor for reconstruction from persistence
    public static Transfer reconstruct(TransferId id, AccountId fromAccountId, AccountId toAccountId, 
                                     Amount amount, IdempotencyKey idempotencyKey, String description,
                                     TransferStatus status, String failureReason) {
        Transfer transfer = new Transfer(id, fromAccountId, toAccountId, amount, idempotencyKey, description);
        transfer.status = status;
        transfer.failureReason = failureReason;
        // Don't fire domain events during reconstruction
        transfer.clearDomainEvents();
        return transfer;
    }

    public void markAsProcessing() {
        if (!status.canTransitionTo(TransferStatus.PROCESSING)) {
            throw new IllegalStateException("Cannot transition to PROCESSING from " + status);
        }
        this.status = TransferStatus.PROCESSING;
        applyChange();
    }

    public void markAsCompleted() {
        if (!status.canTransitionTo(TransferStatus.COMPLETED)) {
            throw new IllegalStateException("Cannot transition to COMPLETED from " + status);
        }
        this.status = TransferStatus.COMPLETED;
        applyChange();
        
        addDomainEvent(new TransferCompleted(
            getId().getValue(),
            fromAccountId.getValue(),
            toAccountId.getValue(),
            amount.getValue()
        ));
    }

    public void markAsFailed(String reason) {
        if (!status.canTransitionTo(TransferStatus.FAILED)) {
            throw new IllegalStateException("Cannot transition to FAILED from " + status);
        }
        this.status = TransferStatus.FAILED;
        this.failureReason = reason;
        applyChange();
        
        addDomainEvent(new TransferFailed(
            getId().getValue(),
            fromAccountId.getValue(),
            toAccountId.getValue(),
            amount.getValue(),
            reason
        ));
    }

    // Getters
    public AccountId getFromAccountId() { return fromAccountId; }
    public AccountId getToAccountId() { return toAccountId; }
    public Amount getAmount() { return amount; }
    public TransferStatus getStatus() { return status; }
    public IdempotencyKey getIdempotencyKey() { return idempotencyKey; }
    public String getDescription() { return description; }
    public String getFailureReason() { return failureReason; }

    @Override
    protected void validateInvariants() {
        if (fromAccountId == null) throw new IllegalStateException("From account ID cannot be null");
        if (toAccountId == null) throw new IllegalStateException("To account ID cannot be null");
        if (amount == null) throw new IllegalStateException("Amount cannot be null");
        if (idempotencyKey == null) throw new IllegalStateException("Idempotency key cannot be null");
        if (fromAccountId.equals(toAccountId)) throw new IllegalStateException("Cannot transfer to the same account");
    }
}