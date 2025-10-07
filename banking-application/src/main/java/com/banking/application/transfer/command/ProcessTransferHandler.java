package com.banking.application.transfer.command;

import com.banking.application.shared.base.CommandHandler;
import com.banking.application.shared.exception.ValidationException;
import com.banking.application.shared.interfaces.EventBus;
import com.banking.application.shared.interfaces.TransactionManager;
import com.banking.application.transfer.dto.TransferResponse;
import com.banking.domain.account.entity.Account;
import com.banking.domain.account.repository.AccountRepository;
import com.banking.domain.account.valueobject.AccountId;
import com.banking.domain.account.valueobject.Balance;
import com.banking.domain.transfer.entity.Transfer;
import com.banking.domain.transfer.repository.TransferRepository;
import com.banking.domain.transfer.valueobject.Amount;
import com.banking.domain.transfer.valueobject.IdempotencyKey;
import org.springframework.stereotype.Service;

/**
 * Handler para processar transferências bancárias.
 * Implementa o padrão Saga para garantir consistência transacional.
 */
@Service
public class ProcessTransferHandler implements CommandHandler<ProcessTransferCommand, TransferResponse> {

    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final EventBus eventBus;
    private final TransactionManager transactionManager;

    public ProcessTransferHandler(AccountRepository accountRepository,
                                 TransferRepository transferRepository,
                                 EventBus eventBus,
                                 TransactionManager transactionManager) {
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.eventBus = eventBus;
        this.transactionManager = transactionManager;
    }

    @Override
    public TransferResponse handle(ProcessTransferCommand command) throws Exception {
        validate(command);

        return transactionManager.executeInTransaction(() -> {
            // Verificar idempotência
            IdempotencyKey idempotencyKey = IdempotencyKey.of(command.getIdempotencyKey());
            if (transferRepository.existsByIdempotencyKey(idempotencyKey)) {
                // Retornar transferência existente
                Transfer existingTransfer = transferRepository.findByIdempotencyKey(idempotencyKey)
                    .orElseThrow(() -> new ValidationException("Idempotency key conflict"));
                return TransferResponse.from(existingTransfer);
            }

            // Criar value objects
            AccountId fromAccountId = AccountId.of(command.getFromAccountId());
            AccountId toAccountId = AccountId.of(command.getToAccountId());
            Amount amount = Amount.of(command.getAmount());

            // Buscar contas
            Account fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new ValidationException("From account not found"));
            Account toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new ValidationException("To account not found"));

            // Validar contas
            validateAccounts(fromAccount, toAccount);

            // Criar transferência
            Transfer transfer = Transfer.create(
                fromAccountId, 
                toAccountId, 
                amount, 
                idempotencyKey, 
                command.getDescription()
            );

            // Salvar transferência como PENDING
            Transfer savedTransfer = transferRepository.save(transfer);

            try {
                // Processar transferência
                transfer.markAsProcessing();
                
                // Realizar débito e crédito
                Balance transferBalance = Balance.of(amount.getValue());
                fromAccount.debit(transferBalance, "Transfer to " + toAccountId.getValue());
                toAccount.credit(transferBalance, "Transfer from " + fromAccountId.getValue());

                // Salvar contas atualizadas
                accountRepository.save(fromAccount);
                accountRepository.save(toAccount);

                // Marcar transferência como concluída
                transfer.markAsCompleted();
                savedTransfer = transferRepository.save(transfer);

                // Publicar eventos
                publishDomainEvents(fromAccount, toAccount, transfer);

                return TransferResponse.from(savedTransfer);

            } catch (Exception e) {
                // Marcar transferência como falhada
                transfer.markAsFailed("Transfer failed: " + e.getMessage());
                transferRepository.save(transfer);
                
                // Publicar eventos de falha
                if (transfer.hasDomainEvents()) {
                    eventBus.publishAll(transfer.getDomainEvents());
                    transfer.clearDomainEvents();
                }
                
                throw new ValidationException("Transfer failed: " + e.getMessage(), e);
            }
        });
    }

    private void validateAccounts(Account fromAccount, Account toAccount) {
        if (!fromAccount.isActive()) {
            throw new ValidationException("From account is not active");
        }
        if (!toAccount.isActive()) {
            throw new ValidationException("To account is not active");
        }
    }

    private void publishDomainEvents(Account fromAccount, Account toAccount, Transfer transfer) {
        // Publicar eventos da conta de origem
        if (fromAccount.hasDomainEvents()) {
            eventBus.publishAll(fromAccount.getDomainEvents());
            fromAccount.clearDomainEvents();
        }

        // Publicar eventos da conta de destino
        if (toAccount.hasDomainEvents()) {
            eventBus.publishAll(toAccount.getDomainEvents());
            toAccount.clearDomainEvents();
        }

        // Publicar eventos da transferência
        if (transfer.hasDomainEvents()) {
            eventBus.publishAll(transfer.getDomainEvents());
            transfer.clearDomainEvents();
        }
    }

    @Override
    public void validate(ProcessTransferCommand command) {
        CommandHandler.super.validate(command);
        
        if (command.request() == null) {
            throw new ValidationException("Transfer request cannot be null");
        }

        // Validações adicionais de negócio
        if (command.getFromAccountId().equals(command.getToAccountId())) {
            throw new ValidationException("Cannot transfer to the same account");
        }

        if (command.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Transfer amount must be positive");
        }
    }

    @Override
    public Class<ProcessTransferCommand> getCommandType() {
        return ProcessTransferCommand.class;
    }
}