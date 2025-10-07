package com.banking.infrastructure.persistence.jpa.repository;

import com.banking.infrastructure.persistence.jpa.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para operações de persistência de contas.
 * Fornece métodos de acesso a dados usando Spring Data JPA.
 */
@Repository
public interface JpaAccountRepository extends JpaRepository<AccountEntity, String> {

    /**
     * Encontra uma conta pelo número da conta
     */
    Optional<AccountEntity> findByAccountNumber(String accountNumber);

    /**
     * Encontra uma conta pelo CPF do titular
     */
    Optional<AccountEntity> findByHolderCpf(String holderCpf);

    /**
     * Verifica se existe uma conta com o número especificado
     */
    boolean existsByAccountNumber(String accountNumber);

    /**
     * Verifica se existe uma conta com o CPF especificado
     */
    boolean existsByHolderCpf(String holderCpf);

    /**
     * Encontra todas as contas ativas
     */
    @Query("SELECT a FROM AccountEntity a WHERE a.active = true ORDER BY a.createdAt DESC")
    List<AccountEntity> findAllActive();

    /**
     * Encontra todas as contas ativas (método Spring Data JPA)
     */
    List<AccountEntity> findByActiveTrue();

    /**
     * Conta o número de contas ativas
     */
    @Query("SELECT COUNT(a) FROM AccountEntity a WHERE a.active = true")
    long countActive();

    /**
     * Conta o número de contas ativas (método Spring Data JPA)
     */
    long countByActiveTrue();

    /**
     * Encontra contas por nome do titular (busca parcial, case insensitive)
     */
    @Query("SELECT a FROM AccountEntity a WHERE LOWER(a.holderName) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY a.holderName")
    List<AccountEntity> findByHolderNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Encontra contas com saldo acima de um valor
     */
    @Query("SELECT a FROM AccountEntity a WHERE a.balance >= :minBalance AND a.active = true ORDER BY a.balance DESC")
    List<AccountEntity> findByBalanceGreaterThanEqual(@Param("minBalance") java.math.BigDecimal minBalance);

    /**
     * Encontra contas criadas em um período específico
     */
    @Query("SELECT a FROM AccountEntity a WHERE a.createdAt BETWEEN :startDate AND :endDate ORDER BY a.createdAt DESC")
    List<AccountEntity> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                              @Param("endDate") java.time.LocalDateTime endDate);

    /**
     * Calcula o saldo total de todas as contas ativas
     */
    @Query("SELECT COALESCE(SUM(a.balance), 0) FROM AccountEntity a WHERE a.active = true")
    java.math.BigDecimal calculateTotalBalance();
}