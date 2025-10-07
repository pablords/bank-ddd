package com.banking.infrastructure.persistence.mapper;

import com.banking.domain.account.entity.Account;
import com.banking.domain.account.valueobject.AccountNumber;
import com.banking.domain.account.valueobject.Balance;
import com.banking.domain.account.valueobject.Cpf;
import com.banking.domain.account.valueobject.AccountId;
import com.banking.domain.account.valueobject.HolderName;
import com.banking.infrastructure.persistence.jpa.entity.AccountEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversão entre entidades de domínio e entidades de persistência de Account.
 * Responsável por traduzir objetos entre as camadas de domínio e infraestrutura.
 */
@Component
public class AccountMapper {

    /**
     * Converte uma entidade de persistência para entidade de domínio
     */
    public Account toDomain(AccountEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Account(
                AccountId.of(entity.getId()),
                AccountNumber.of(entity.getAccountNumber()),
                HolderName.of(entity.getHolderName()),
                Cpf.of(entity.getHolderCpf()),
                Balance.of(entity.getBalance()),
                entity.getActive()
        );
    }

    /**
     * Converte uma entidade de domínio para entidade de persistência
     */
    public AccountEntity toEntity(Account domain) {
        if (domain == null) {
            return null;
        }

        AccountEntity entity = new AccountEntity();
        entity.setId(domain.getId().getValue());
        entity.setAccountNumber(domain.getAccountNumber().getValue());
        entity.setHolderName(domain.getHolderName().getValue());
        entity.setHolderCpf(domain.getHolderCpf().getValue());
        entity.setBalance(domain.getBalance().getAmount());
        entity.setActive(domain.isActive());

        return entity;
    }

    /**
     * Atualiza uma entidade de persistência existente com dados do domínio
     */
    public void updateEntity(AccountEntity entity, Account domain) {
        if (entity == null || domain == null) {
            return;
        }

        entity.setAccountNumber(domain.getAccountNumber().getValue());
        entity.setHolderName(domain.getHolderName().getValue());
        entity.setHolderCpf(domain.getHolderCpf().getValue());
        entity.setBalance(domain.getBalance().getAmount());
        entity.setActive(domain.isActive());
    }
}