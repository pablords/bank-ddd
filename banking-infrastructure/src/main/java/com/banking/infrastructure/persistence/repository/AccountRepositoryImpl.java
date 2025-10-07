package com.banking.infrastructure.persistence.repository;

import com.banking.domain.entity.Account;
import com.banking.domain.repository.AccountRepository;
import com.banking.domain.value_object.AccountNumber;
import com.banking.domain.value_object.CPF;
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
    public Optional<Account> findById(String id) {
        return jpaAccountRepository.findById(id)
                .map(accountMapper::toDomain);
    }

    @Override
    public Optional<Account> findByAccountNumber(AccountNumber accountNumber) {
        return jpaAccountRepository.findByAccountNumber(accountNumber.getValue())
                .map(accountMapper::toDomain);
    }

    @Override
    public boolean existsByCpf(CPF cpf) {
        return jpaAccountRepository.existsByHolderCpf(cpf.getValue());
    }

    @Override
    public boolean existsByAccountNumber(AccountNumber accountNumber) {
        return jpaAccountRepository.existsByAccountNumber(accountNumber.getValue());
    }

    @Override
    public List<Account> findAll() {
        return jpaAccountRepository.findAll()
                .stream()
                .map(accountMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> findActiveAccounts() {
        return jpaAccountRepository.findByIsActiveTrue()
                .stream()
                .map(accountMapper::toDomain)
                .collect(Collectors.toList());
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
    public void deleteById(String id) {
        jpaAccountRepository.deleteById(id);
    }

    /**
     * Métodos específicos de negócio não definidos na interface do domínio
     */
    
    public long countActiveAccounts() {
        return jpaAccountRepository.countByIsActiveTrue();
    }

    public java.math.BigDecimal calculateTotalBalance() {
        return jpaAccountRepository.calculateTotalBalance();
    }

    public List<Account> findAccountsWithLowBalance(java.math.BigDecimal threshold) {
        return jpaAccountRepository.findByBalanceLessThan(threshold)
                .stream()
                .map(accountMapper::toDomain)
                .collect(Collectors.toList());
    }
}