package com.banking.infrastructure.persistence.jpa.repository;

import com.banking.infrastructure.persistence.jpa.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório JPA para operações de persistência de transferências.
 * Fornece métodos de acesso a dados usando Spring Data JPA.
 */
@Repository
public interface JpaTransferRepository extends JpaRepository<TransferEntity, String> {

    /**
     * Encontra uma transferência pela chave de idempotência
     */
    Optional<TransferEntity> findByIdempotencyKey(String idempotencyKey);

    /**
     * Verifica se existe uma transferência com a chave de idempotência
     */
    boolean existsByIdempotencyKey(String idempotencyKey);

    /**
     * Encontra transferências por conta de origem
     */
    @Query("SELECT t FROM TransferEntity t WHERE t.fromAccountId = :accountId ORDER BY t.createdAt DESC")
    List<TransferEntity> findByFromAccountId(@Param("accountId") String accountId);

    /**
     * Encontra transferências por conta de destino
     */
    @Query("SELECT t FROM TransferEntity t WHERE t.toAccountId = :accountId ORDER BY t.createdAt DESC")
    List<TransferEntity> findByToAccountId(@Param("accountId") String accountId);

    /**
     * Encontra transferências por status
     */
    List<TransferEntity> findByStatus(TransferEntity.TransferStatusEnum status);

    /**
     * Encontra histórico de transferências de uma conta (origem ou destino)
     */
    @Query("SELECT t FROM TransferEntity t WHERE t.fromAccountId = :accountId OR t.toAccountId = :accountId ORDER BY t.createdAt DESC")
    List<TransferEntity> findTransferHistory(@Param("accountId") String accountId);

    /**
     * Encontra transferências em um período específico
     */
    @Query("SELECT t FROM TransferEntity t WHERE t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<TransferEntity> findByCreatedAtBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                               @Param("endDate") java.time.LocalDateTime endDate);

    /**
     * Encontra transferências pendentes há mais de X minutos
     */
    @Query("SELECT t FROM TransferEntity t WHERE t.status = 'PENDING' AND t.createdAt < :cutoffTime ORDER BY t.createdAt")
    List<TransferEntity> findPendingTransfersOlderThan(@Param("cutoffTime") java.time.LocalDateTime cutoffTime);

    /**
     * Conta transferências por status
     */
    @Query("SELECT COUNT(t) FROM TransferEntity t WHERE t.status = :status")
    long countByStatus(@Param("status") TransferEntity.TransferStatusEnum status);

    /**
     * Calcula valor total transferido em um período
     */
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM TransferEntity t WHERE t.status = 'COMPLETED' AND t.createdAt BETWEEN :startDate AND :endDate")
    java.math.BigDecimal calculateTotalTransferredBetween(@Param("startDate") java.time.LocalDateTime startDate, 
                                                         @Param("endDate") java.time.LocalDateTime endDate);

    /**
     * Encontra transferências com falha para análise
     */
    @Query("SELECT t FROM TransferEntity t WHERE t.status = 'FAILED' ORDER BY t.updatedAt DESC")
    List<TransferEntity> findFailedTransfers();
}