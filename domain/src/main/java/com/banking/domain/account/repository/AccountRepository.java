package com.banking.domain.account.repository;

import com.banking.domain.account.entity.Account;
import com.banking.domain.account.valueobject.AccountId;
import com.banking.domain.account.valueobject.AccountNumber;
import com.banking.domain.account.valueobject.Cpf;
import com.banking.domain.shared.interfaces.Repository;

import java.util.Optional;

/**
 * Repository interface para o agregado Account.
 * Define operações específicas de persistência para contas.
 */
public interface AccountRepository extends Repository<Account, AccountId> {

    /**
     * Encontra uma conta pelo número da conta
     */
    Optional<Account> findByAccountNumber(AccountNumber accountNumber);

    /**
     * Encontra uma conta pelo CPF do titular
     */
    Optional<Account> findByHolderCpf(Cpf cpf);

    /**
     * Verifica se existe uma conta com o número especificado
     */
    boolean existsByAccountNumber(AccountNumber accountNumber);

    /**
     * Verifica se existe uma conta com o CPF especificado
     */
    boolean existsByHolderCpf(Cpf cpf);

    /**
     * Encontra todas as contas ativas
     */
    java.util.List<Account> findAllActive();

    /**
     * Conta o número de contas ativas
     */
    long countActive();
}