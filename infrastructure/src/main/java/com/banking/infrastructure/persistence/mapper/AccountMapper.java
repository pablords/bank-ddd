package com.banking.infrastructure.persistence.mapper;

import com.banking.domain.entity.Account;
import com.banking.domain.value_object.AccountNumber;
import com.banking.domain.value_object.Balance;
import com.banking.domain.value_object.CPF;
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

        return Account.builder()
                .id(entity.getId())
                .accountNumber(new AccountNumber(entity.getAccountNumber()))
                .holderName(entity.getHolderName())
                .holderCpf(new CPF(entity.getHolderCpf()))
                .balance(new Balance(entity.getBalance()))
                .isActive(entity.getIsActive())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    /**
     * Converte uma entidade de domínio para entidade de persistência
     */
    public AccountEntity toEntity(Account domain) {
        if (domain == null) {
            return null;
        }

        AccountEntity entity = new AccountEntity();
        entity.setId(domain.getId());
        entity.setAccountNumber(domain.getAccountNumber().getValue());
        entity.setHolderName(domain.getHolderName());
        entity.setHolderCpf(domain.getHolderCpf().getValue());
        entity.setBalance(domain.getBalance().getValue());
        entity.setIsActive(domain.getIsActive());
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(java.time.LocalDateTime.now());

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
        entity.setHolderName(domain.getHolderName());
        entity.setHolderCpf(domain.getHolderCpf().getValue());
        entity.setBalance(domain.getBalance().getValue());
        entity.setIsActive(domain.getIsActive());
        entity.setUpdatedAt(java.time.LocalDateTime.now());
    }
}