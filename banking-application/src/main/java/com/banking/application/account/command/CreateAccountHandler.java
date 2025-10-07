package com.banking.application.account.command;

import com.banking.application.account.dto.AccountResponse;
import com.banking.application.shared.base.CommandHandler;
import com.banking.application.shared.exception.ValidationException;
import com.banking.application.shared.interfaces.EventBus;
import com.banking.application.shared.interfaces.TransactionManager;
import com.banking.domain.account.entity.Account;
import com.banking.domain.account.repository.AccountRepository;
import com.banking.domain.account.valueobject.*;
import org.springframework.stereotype.Service;

/**
 * Handler para processar o comando de criação de conta.
 */
@Service
public class CreateAccountHandler implements CommandHandler<CreateAccountCommand, AccountResponse> {

    private final AccountRepository accountRepository;
    private final EventBus eventBus;
    private final TransactionManager transactionManager;

    public CreateAccountHandler(AccountRepository accountRepository, 
                               EventBus eventBus,
                               TransactionManager transactionManager) {
        this.accountRepository = accountRepository;
        this.eventBus = eventBus;
        this.transactionManager = transactionManager;
    }

    @Override
    public AccountResponse handle(CreateAccountCommand command) throws Exception {
        validate(command);
        
        return transactionManager.executeInTransaction(() -> {
            // Extrair dados do command
            String holderName = command.getHolderName();
            String holderCpf = command.getHolderCpf();
            java.math.BigDecimal initialBalance = command.getInitialBalance();

            // Criar value objects do domínio
            HolderName name = HolderName.of(holderName);
            Cpf cpf = Cpf.of(holderCpf);
            Balance balance = Balance.of(initialBalance);

            // Verificar se já existe conta com este CPF
            if (accountRepository.existsByHolderCpf(cpf)) {
                throw new ValidationException("CPF already has an associated account");
            }

            // Gerar número de conta único
            AccountNumber accountNumber = generateUniqueAccountNumber();

            // Criar nova conta
            Account account = Account.create(accountNumber, name, cpf, balance);

            // Salvar no repositório
            Account savedAccount = accountRepository.save(account);

            // Publicar eventos de domínio
            if (savedAccount.hasDomainEvents()) {
                eventBus.publishAll(savedAccount.getDomainEvents());
                savedAccount.clearDomainEvents();
            }

            // Retornar resposta
            return AccountResponse.from(savedAccount);
        });
    }

    @Override
    public void validate(CreateAccountCommand command) {
        CommandHandler.super.validate(command);
        
        if (command.request() == null) {
            throw new ValidationException("Account creation request cannot be null");
        }

        CreateAccountRequest request = command.request();
        
        // Validações adicionais de negócio
        if (request.holderName() == null || request.holderName().trim().isEmpty()) {
            throw new ValidationException("Holder name is required");
        }

        if (request.holderCpf() == null || request.holderCpf().trim().isEmpty()) {
            throw new ValidationException("CPF is required");
        }

        if (request.initialBalance() != null && request.initialBalance().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new ValidationException("Initial balance cannot be negative");
        }
    }

    /**
     * Gera um número de conta único
     */
    private AccountNumber generateUniqueAccountNumber() {
        AccountNumber accountNumber;
        int attempts = 0;
        int maxAttempts = 10;

        do {
            accountNumber = AccountNumber.generate();
            attempts++;
            
            if (attempts >= maxAttempts) {
                throw new RuntimeException("Unable to generate unique account number after " + maxAttempts + " attempts");
            }
        } while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    @Override
    public Class<CreateAccountCommand> getCommandType() {
        return CreateAccountCommand.class;
    }
}