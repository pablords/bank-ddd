package com.banking.infrastructure.persistence.repository;

import com.banking.domain.entity.Transfer;
import com.banking.domain.repository.TransferRepository;
import com.banking.domain.value_object.IdempotencyKey;
import com.banking.infrastructure.persistence.jpa.entity.TransferEntity;
import com.banking.infrastructure.persistence.jpa.repository.JpaTransferRepository;
import com.banking.infrastructure.persistence.mapper.TransferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementação do repositório de transferências usando JPA.
 * Traduz entre entidades do domínio e entidades de persistência.
 */
@Repository
public class TransferRepositoryImpl implements TransferRepository {

    private final JpaTransferRepository jpaTransferRepository;
    private final TransferMapper transferMapper;

    @Autowired
    public TransferRepositoryImpl(JpaTransferRepository jpaTransferRepository, TransferMapper transferMapper) {
        this.jpaTransferRepository = jpaTransferRepository;
        this.transferMapper = transferMapper;
    }

    @Override
    public Optional<Transfer> findById(String id) {
        return jpaTransferRepository.findById(id)
                .map(transferMapper::toDomain);
    }

    @Override
    public Optional<Transfer> findByIdempotencyKey(IdempotencyKey idempotencyKey) {
        return jpaTransferRepository.findByIdempotencyKey(idempotencyKey.getValue())
                .map(transferMapper::toDomain);
    }

    @Override
    public boolean existsByIdempotencyKey(IdempotencyKey idempotencyKey) {
        return jpaTransferRepository.existsByIdempotencyKey(idempotencyKey.getValue());
    }

    @Override
    public List<Transfer> findByFromAccountId(String accountId) {
        return jpaTransferRepository.findByFromAccountId(accountId)
                .stream()
                .map(transferMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findByToAccountId(String accountId) {
        return jpaTransferRepository.findByToAccountId(accountId)
                .stream()
                .map(transferMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findTransferHistory(String accountId) {
        return jpaTransferRepository.findTransferHistory(accountId)
                .stream()
                .map(transferMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Transfer save(Transfer transfer) {
        TransferEntity entity = transferMapper.toEntity(transfer);
        TransferEntity savedEntity = jpaTransferRepository.save(entity);
        return transferMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Transfer transfer) {
        TransferEntity entity = transferMapper.toEntity(transfer);
        jpaTransferRepository.delete(entity);
    }

    @Override
    public void deleteById(String id) {
        jpaTransferRepository.deleteById(id);
    }

    @Override
    public List<Transfer> findAll() {
        return jpaTransferRepository.findAll()
                .stream()
                .map(transferMapper::toDomain)
                .collect(Collectors.toList());
    }

    /**
     * Métodos específicos de infraestrutura para operações administrativas
     */
    
    public List<Transfer> findPendingTransfersOlderThan(LocalDateTime cutoffTime) {
        return jpaTransferRepository.findPendingTransfersOlderThan(cutoffTime)
                .stream()
                .map(transferMapper::toDomain)
                .collect(Collectors.toList());
    }

    public List<Transfer> findFailedTransfers() {
        return jpaTransferRepository.findFailedTransfers()
                .stream()
                .map(transferMapper::toDomain)
                .collect(Collectors.toList());
    }

    public java.math.BigDecimal calculateTotalTransferredBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return jpaTransferRepository.calculateTotalTransferredBetween(startDate, endDate);
    }
}