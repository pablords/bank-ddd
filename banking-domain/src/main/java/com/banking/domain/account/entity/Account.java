package com.banking.domain.account.entity;

import com.banking.domain.account.event.AccountCreated;
import com.banking.domain.account.event.AccountCredited;
import com.banking.domain.account.event.AccountDebited;
import com.banking.domain.account.exception.InsufficientFundsException;
import com.banking.domain.account.valueobject.*;
import com.banking.domain.shared.base.AggregateRoot;

/**
 * Aggregate Root que representa uma conta bancária.
 * Centraliza todas as operações e regras de negócio relacionadas à conta.
 */
public class Account extends AggregateRoot<AccountId> {

    private AccountNumber accountNumber;
    private HolderName holderName;
    private Cpf holderCpf;
    private Balance balance;
    private boolean active;

    // Construtor para criação
    private Account(AccountId id, AccountNumber accountNumber, HolderName holderName, Cpf holderCpf, Balance initialBalance) {
        super(id);
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.holderCpf = holderCpf;
        this.balance = initialBalance != null ? initialBalance : Balance.zero();
        this.active = true;
        
        validateInvariants();
        addDomainEvent(new AccountCreated(
            id.getValue(),
            accountNumber.getValue(),
            holderName.getValue(),
            holderCpf.getValue(),
            this.balance.getAmount()
        ));
    }

    // Construtor para reconstrução (usado pela infraestrutura)
    public Account(AccountId id, AccountNumber accountNumber, HolderName holderName, 
                   Cpf holderCpf, Balance balance, boolean active) {
        super(id);
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.holderCpf = holderCpf;
        this.balance = balance;
        this.active = active;
        validateInvariants();
    }

    /**
     * Factory method para criar nova conta
     */
    public static Account create(AccountNumber accountNumber, HolderName holderName, 
                                Cpf holderCpf, Balance initialBalance) {
        AccountId id = AccountId.generate();
        return new Account(id, accountNumber, holderName, holderCpf, initialBalance);
    }

    /**
     * Factory method para criar conta com saldo zero
     */
    public static Account create(AccountNumber accountNumber, HolderName holderName, Cpf holderCpf) {
        return create(accountNumber, holderName, holderCpf, Balance.zero());
    }

    /**
     * Realiza débito na conta
     */
    public void debit(Balance amount, String reason) {
        if (amount == null) {
            throw new IllegalArgumentException("Debit amount cannot be null");
        }

        if (!amount.isPositive()) {
            throw new IllegalArgumentException("Debit amount must be positive");
        }

        if (!isActive()) {
            throw new IllegalStateException("Cannot debit from inactive account");
        }

        if (!balance.hasSufficientFunds(amount)) {
            throw new InsufficientFundsException(
                String.format("Insufficient funds. Current balance: %s, Required: %s", 
                    balance, amount));
        }

        this.balance = this.balance.subtract(amount);
        applyChange();
        
        addDomainEvent(new AccountDebited(
            getId().getValue(),
            amount.getAmount(),
            this.balance.getAmount(),
            reason
        ));
    }

    /**
     * Realiza crédito na conta
     */
    public void credit(Balance amount, String reason) {
        if (amount == null) {
            throw new IllegalArgumentException("Credit amount cannot be null");
        }

        if (!amount.isPositive()) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }

        if (!isActive()) {
            throw new IllegalStateException("Cannot credit to inactive account");
        }

        this.balance = this.balance.add(amount);
        applyChange();
        
        addDomainEvent(new AccountCredited(
            getId().getValue(),
            amount.getAmount(),
            this.balance.getAmount(),
            reason
        ));
    }

    /**
     * Ativa a conta
     */
    public void activate() {
        this.active = true;
        applyChange();
    }

    /**
     * Desativa a conta
     */
    public void deactivate() {
        if (balance.isPositive()) {
            throw new IllegalStateException("Cannot deactivate account with positive balance");
        }
        this.active = false;
        applyChange();
    }

    /**
     * Verifica se a conta tem saldo suficiente
     */
    public boolean hasSufficientFunds(Balance amount) {
        return balance.hasSufficientFunds(amount);
    }

    // Getters
    public AccountNumber getAccountNumber() {
        return accountNumber;
    }

    public HolderName getHolderName() {
        return holderName;
    }

    public Cpf getHolderCpf() {
        return holderCpf;
    }

    public Balance getBalance() {
        return balance;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    protected void validateInvariants() {
        if (accountNumber == null) {
            throw new IllegalStateException("Account number cannot be null");
        }
        if (holderName == null) {
            throw new IllegalStateException("Holder name cannot be null");
        }
        if (holderCpf == null) {
            throw new IllegalStateException("Holder CPF cannot be null");
        }
        if (balance == null) {
            throw new IllegalStateException("Balance cannot be null");
        }
    }

    @Override
    public String toString() {
        return String.format("Account{id=%s, accountNumber=%s, holderName=%s, balance=%s, active=%s}", 
            getId(), accountNumber, holderName, balance, active);
    }
}