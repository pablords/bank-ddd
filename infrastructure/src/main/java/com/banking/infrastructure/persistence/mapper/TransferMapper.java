package com.banking.infrastructure.persistence.mapper;

import com.banking.domain.transfer.entity.Transfer;
import com.banking.domain.transfer.valueobject.Amount;
import com.banking.domain.transfer.valueobject.IdempotencyKey;
import com.banking.domain.transfer.valueobject.TransferStatus;
import com.banking.domain.account.valueobject.AccountId;
import com.banking.domain.transfer.valueobject.TransferId;
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

        return Transfer.reconstruct(
                TransferId.of(entity.getId()),
                AccountId.of(entity.getFromAccountId()),
                AccountId.of(entity.getToAccountId()),
                Amount.of(entity.getAmount()),
                IdempotencyKey.of(entity.getIdempotencyKey()),
                entity.getDescription(),
                mapStatusToDomain(entity.getStatus()),
                entity.getFailureReason()
        );
    }

    /**
     * Converte uma entidade de domínio para entidade de persistência
     */
    public TransferEntity toEntity(Transfer domain) {
        if (domain == null) {
            return null;
        }

        TransferEntity entity = new TransferEntity();
        entity.setId(domain.getId().getValue());
        entity.setFromAccountId(domain.getFromAccountId().getValue());
        entity.setToAccountId(domain.getToAccountId().getValue());
        entity.setAmount(domain.getAmount().getValue());
        entity.setDescription(domain.getDescription());
        entity.setIdempotencyKey(domain.getIdempotencyKey().getValue());
        entity.setStatus(mapStatusToEntity(domain.getStatus()));
        entity.setFailureReason(domain.getFailureReason());

        return entity;
    }

    /**
     * Atualiza uma entidade de persistência existente com dados do domínio
     */
    public void updateEntity(TransferEntity entity, Transfer domain) {
        if (entity == null || domain == null) {
            return;
        }

        entity.setFromAccountId(domain.getFromAccountId().getValue());
        entity.setToAccountId(domain.getToAccountId().getValue());
        entity.setAmount(domain.getAmount().getValue());
        entity.setDescription(domain.getDescription());
        entity.setIdempotencyKey(domain.getIdempotencyKey().getValue());
        entity.setStatus(mapStatusToEntity(domain.getStatus()));
        entity.setFailureReason(domain.getFailureReason());
    }

    /**
     * Mapeia status da entidade de persistência para domínio
     */
    private TransferStatus mapStatusToDomain(TransferEntity.TransferStatusEnum entityStatus) {
        if (entityStatus == null) {
            return null;
        }

        switch (entityStatus) {
            case PENDING:
                return TransferStatus.PENDING;
            case PROCESSING:
                return TransferStatus.PROCESSING;
            case COMPLETED:
                return TransferStatus.COMPLETED;
            case FAILED:
                return TransferStatus.FAILED;
            case CANCELLED:
                return TransferStatus.CANCELLED;
            default:
                throw new IllegalArgumentException("Status de transferência não reconhecido: " + entityStatus);
        }
    }

    /**
     * Mapeia status do domínio para entidade de persistência
     */
    private TransferEntity.TransferStatusEnum mapStatusToEntity(TransferStatus domainStatus) {
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