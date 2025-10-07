package com.banking.application.account.query;

import com.banking.application.account.dto.AccountBalanceResponse;
import com.banking.application.shared.base.QueryHandler;
import com.banking.application.shared.exception.ValidationException;
import com.banking.application.shared.interfaces.CacheService;
import com.banking.domain.account.entity.Account;
import com.banking.domain.account.repository.AccountRepository;
import com.banking.domain.account.valueobject.AccountId;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

/**
 * Handler para processar consulta de saldo de conta.
 */
@Service
public class GetAccountBalanceHandler implements QueryHandler<GetAccountBalanceQuery, AccountBalanceResponse> {

    private final AccountRepository accountRepository;
    private final CacheService cacheService;

    public GetAccountBalanceHandler(AccountRepository accountRepository, CacheService cacheService) {
        this.accountRepository = accountRepository;
        this.cacheService = cacheService;
    }

    @Override
    public AccountBalanceResponse handle(GetAccountBalanceQuery query) throws Exception {
        validate(query);

        // Tentar buscar no cache primeiro
        String cacheKey = query.getCacheKey();
        Optional<AccountBalanceResponse> cachedResult = cacheService.get(cacheKey, AccountBalanceResponse.class);
        
        if (cachedResult.isPresent()) {
            return cachedResult.get();
        }

        // Buscar no repositório
        AccountId accountId = AccountId.of(query.accountId());
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new ValidationException("Account not found with ID: " + query.accountId()));

        // Verificar se a conta está ativa
        if (!account.isActive()) {
            throw new ValidationException("Cannot retrieve balance from inactive account");
        }

        // Converter para DTO
        AccountBalanceResponse response = AccountBalanceResponse.from(account);

        // Armazenar no cache com TTL curto (saldo muda frequentemente)
        if (shouldCache(query)) {
            Duration ttl = Duration.ofSeconds(query.getCacheTtlSeconds());
            cacheService.put(cacheKey, response, ttl);
        }

        return response;
    }

    @Override
    public void validate(GetAccountBalanceQuery query) {
        QueryHandler.super.validate(query);
        
        if (query.accountId() == null || query.accountId().trim().isEmpty()) {
            throw new ValidationException("Account ID cannot be null or empty");
        }

        // Validar formato UUID
        try {
            java.util.UUID.fromString(query.accountId());
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Account ID must be a valid UUID");
        }
    }

    @Override
    public Class<GetAccountBalanceQuery> getQueryType() {
        return GetAccountBalanceQuery.class;
    }
}