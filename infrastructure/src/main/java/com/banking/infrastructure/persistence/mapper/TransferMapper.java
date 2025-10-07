package com.banking.infrastructure.persistence.mapper;

import com.banking.domain.entity.Transfer;
import com.banking.domain.value_object.Amount;
import com.banking.domain.value_object.IdempotencyKey;
import com.banking.infrastructure.persistence.jpa.entity.TransferEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversão entre entidades de domínio e entidades de persistência de Transfer.
 * Responsável por traduzir objetos entre as camadas de domínio e infraestrutura.
 */
@Component
public class TransferMapper {

    /**
     * Converte uma entidade de persistência para entidade de domínio
     */
    public Transfer toDomain(TransferEntity entity) {
        if (entity == null) {
            return null;
        }

        return Transfer.builder()
                .id(entity.getId())
                .fromAccountId(entity.getFromAccountId())
                .toAccountId(entity.getToAccountId())
                .amount(new Amount(entity.getAmount()))
                .description(entity.getDescription())
                .idempotencyKey(new IdempotencyKey(entity.getIdempotencyKey()))
                .status(mapStatusToDomain(entity.getStatus()))
                .createdAt(entity.getCreatedAt())
                .build();
    }

    /**
     * Converte uma entidade de domínio para entidade de persistência
     */
    public TransferEntity toEntity(Transfer domain) {
        if (domain == null) {
            return null;
        }

        TransferEntity entity = new TransferEntity();
        entity.setId(domain.getId());
        entity.setFromAccountId(domain.getFromAccountId());
        entity.setToAccountId(domain.getToAccountId());
        entity.setAmount(domain.getAmount().getValue());
        entity.setDescription(domain.getDescription());
        entity.setIdempotencyKey(domain.getIdempotencyKey().getValue());
        entity.setStatus(mapStatusToEntity(domain.getStatus()));
        entity.setCreatedAt(domain.getCreatedAt());
        entity.setUpdatedAt(java.time.LocalDateTime.now());

        return entity;
    }

    /**
     * Atualiza uma entidade de persistência existente com dados do domínio
     */
    public void updateEntity(TransferEntity entity, Transfer domain) {
        if (entity == null || domain == null) {
            return;
        }

        entity.setFromAccountId(domain.getFromAccountId());
        entity.setToAccountId(domain.getToAccountId());
        entity.setAmount(domain.getAmount().getValue());
        entity.setDescription(domain.getDescription());
        entity.setIdempotencyKey(domain.getIdempotencyKey().getValue());
        entity.setStatus(mapStatusToEntity(domain.getStatus()));
        entity.setUpdatedAt(java.time.LocalDateTime.now());
    }

    /**
     * Mapeia status da entidade de persistência para domínio
     */
    private Transfer.TransferStatus mapStatusToDomain(TransferEntity.TransferStatusEnum entityStatus) {
        if (entityStatus == null) {
            return null;
        }

        switch (entityStatus) {
            case PENDING:
                return Transfer.TransferStatus.PENDING;
            case PROCESSING:
                return Transfer.TransferStatus.PROCESSING;
            case COMPLETED:
                return Transfer.TransferStatus.COMPLETED;
            case FAILED:
                return Transfer.TransferStatus.FAILED;
            case CANCELLED:
                return Transfer.TransferStatus.CANCELLED;
            default:
                throw new IllegalArgumentException("Status de transferência não reconhecido: " + entityStatus);
        }
    }

    /**
     * Mapeia status do domínio para entidade de persistência
     */
    private TransferEntity.TransferStatusEnum mapStatusToEntity(Transfer.TransferStatus domainStatus) {
        if (domainStatus == null) {
            return null;
        }

        switch (domainStatus) {
            case PENDING:
                return TransferEntity.TransferStatusEnum.PENDING;
            case PROCESSING:
                return TransferEntity.TransferStatusEnum.PROCESSING;
            case COMPLETED:
                return TransferEntity.TransferStatusEnum.COMPLETED;
            case FAILED:
                return TransferEntity.TransferStatusEnum.FAILED;
            case CANCELLED:
                return TransferEntity.TransferStatusEnum.CANCELLED;
            default:
                throw new IllegalArgumentException("Status de transferência não reconhecido: " + domainStatus);
        }
    }
}