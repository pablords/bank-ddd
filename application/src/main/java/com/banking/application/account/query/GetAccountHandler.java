package com.banking.application.account.query;

import com.banking.application.account.dto.AccountResponse;
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
 * Handler para processar consulta de conta por ID.
 */
@Service
public class GetAccountHandler implements QueryHandler<GetAccountQuery, AccountResponse> {

    private final AccountRepository accountRepository;
    private final CacheService cacheService;

    public GetAccountHandler(AccountRepository accountRepository, CacheService cacheService) {
        this.accountRepository = accountRepository;
        this.cacheService = cacheService;
    }

    @Override
    public AccountResponse handle(GetAccountQuery query) throws Exception {
        validate(query);

        // Tentar buscar no cache primeiro
        String cacheKey = query.getCacheKey();
        Optional<AccountResponse> cachedResult = cacheService.get(cacheKey, AccountResponse.class);
        
        if (cachedResult.isPresent()) {
            return cachedResult.get();
        }

        // Buscar no repositório
        AccountId accountId = AccountId.of(query.accountId());
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new ValidationException("Account not found with ID: " + query.accountId()));

        // Converter para DTO
        AccountResponse response = AccountResponse.from(account);

        // Armazenar no cache
        if (shouldCache(query)) {
            Duration ttl = Duration.ofSeconds(query.getCacheTtlSeconds());
            cacheService.put(cacheKey, response, ttl);
        }

        return response;
    }

    @Override
    public void validate(GetAccountQuery query) {
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
    public Class<GetAccountQuery> getQueryType() {
        return GetAccountQuery.class;
    }
}