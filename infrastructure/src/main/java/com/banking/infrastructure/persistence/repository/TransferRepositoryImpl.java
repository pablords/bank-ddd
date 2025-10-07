package com.banking.infrastructure.persistence.repository;

import com.banking.domain.transfer.entity.Transfer;
import com.banking.domain.transfer.repository.TransferRepository;
import com.banking.domain.transfer.valueobject.IdempotencyKey;
import com.banking.domain.transfer.valueobject.TransferId;
import com.banking.domain.transfer.valueobject.TransferStatus;
import com.banking.domain.account.valueobject.AccountId;
import com.banking.infrastructure.persistence.jpa.entity.TransferEntity;
import com.banking.infrastructure.persistence.jpa.repository.JpaTransferRepository;
import com.banking.infrastructure.persistence.mapper.TransferMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Optional<Transfer> findById(TransferId id) {
        return jpaTransferRepository.findById(id.getValue())
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
    public boolean existsById(TransferId id) {
        return jpaTransferRepository.existsById(id.getValue());
    }

    @Override
    public List<Transfer> findByFromAccountId(AccountId fromAccountId) {
        return jpaTransferRepository.findByFromAccountId(fromAccountId.getValue())
                .stream()
                .map(transferMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findByToAccountId(AccountId accountId) {
        return jpaTransferRepository.findByToAccountId(accountId.getValue())
                .stream()
                .map(transferMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findTransferHistory(AccountId accountId) {
        return jpaTransferRepository.findTransferHistory(accountId.getValue())
                .stream()
                .map(transferMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public long count() {
        return jpaTransferRepository.count();
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
    public void deleteById(TransferId id) {
        jpaTransferRepository.deleteById(id.getValue());
    }

    @Override
    public List<Transfer> findAll() {
        return jpaTransferRepository.findAll()
                .stream()
                .map(transferMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Transfer> findByStatus(TransferStatus status) {
        return jpaTransferRepository.findByStatus(mapStatusToEntity(status))
                .stream()
                .map(transferMapper::toDomain)
                .collect(Collectors.toList());
    }

    private TransferEntity.TransferStatusEnum mapStatusToEntity(TransferStatus status) {
        switch (status) {
            case PENDING: return TransferEntity.TransferStatusEnum.PENDING;
            case PROCESSING: return TransferEntity.TransferStatusEnum.PROCESSING;
            case COMPLETED: return TransferEntity.TransferStatusEnum.COMPLETED;
            case FAILED: return TransferEntity.TransferStatusEnum.FAILED;
            case CANCELLED: return TransferEntity.TransferStatusEnum.CANCELLED;
            default: throw new IllegalArgumentException("Unknown status: " + status);
        }
    }
}
