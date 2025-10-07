package com.banking.infrastructure.persistence.repository;

import com.banking.domain.account.entity.Account;
import com.banking.domain.account.repository.AccountRepository;
import com.banking.domain.account.valueobject.AccountNumber;
import com.banking.domain.account.valueobject.Cpf;
import com.banking.domain.account.valueobject.AccountId;
import com.banking.infrastructure.persistence.jpa.entity.AccountEntity;
import com.banking.infrastructure.persistence.jpa.repository.JpaAccountRepository;
import com.banking.infrastructure.persistence.mapper.AccountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do repositório de contas usando JPA.
 * Traduz entre entidades do domínio e entidades de persistência.
 */
@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final JpaAccountRepository jpaAccountRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountRepositoryImpl(JpaAccountRepository jpaAccountRepository, AccountMapper accountMapper) {
        this.jpaAccountRepository = jpaAccountRepository;
        this.accountMapper = accountMapper;
    }

    @Override
    public Optional<Account> findById(AccountId id) {
        return jpaAccountRepository.findById(id.getValue())
                .map(accountMapper::toDomain);
    }

    @Override
    public Optional<Account> findByAccountNumber(AccountNumber accountNumber) {
        return jpaAccountRepository.findByAccountNumber(accountNumber.getValue())
                .map(accountMapper::toDomain);
    }

    @Override
    public Optional<Account> findByHolderCpf(Cpf cpf) {
        return jpaAccountRepository.findByHolderCpf(cpf.getValue())
                .map(accountMapper::toDomain);
    }

    @Override
    public boolean existsByHolderCpf(Cpf cpf) {
        return jpaAccountRepository.existsByHolderCpf(cpf.getValue());
    }

    @Override
    public boolean existsByAccountNumber(AccountNumber accountNumber) {
        return jpaAccountRepository.existsByAccountNumber(accountNumber.getValue());
    }

    @Override
    public boolean existsById(AccountId id) {
        return jpaAccountRepository.existsById(id.getValue());
    }

    @Override
    public List<Account> findAll() {
        return jpaAccountRepository.findAll()
                .stream()
                .map(accountMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> findAllActive() {
        return jpaAccountRepository.findByActiveTrue()
                .stream()
                .map(accountMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long countActive() {
        return jpaAccountRepository.countByActiveTrue();
    }

    @Override
    public long count() {
        return jpaAccountRepository.count();
    }

    @Override
    public Account save(Account account) {
        AccountEntity entity = accountMapper.toEntity(account);
        AccountEntity savedEntity = jpaAccountRepository.save(entity);
        return accountMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Account account) {
        AccountEntity entity = accountMapper.toEntity(account);
        jpaAccountRepository.delete(entity);
    }

    @Override
    public void deleteById(AccountId id) {
        jpaAccountRepository.deleteById(id.getValue());
    }

    /**
     * Métodos específicos de negócio não definidos na interface do domínio
     */

    public java.math.BigDecimal calculateTotalBalance() {
        return jpaAccountRepository.calculateTotalBalance();
    }
}