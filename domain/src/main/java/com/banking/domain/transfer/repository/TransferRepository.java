package com.banking.domain.transfer.repository;

import com.banking.domain.account.valueobject.AccountId;
import com.banking.domain.shared.interfaces.Repository;
import com.banking.domain.transfer.entity.Transfer;
import com.banking.domain.transfer.valueobject.IdempotencyKey;
import com.banking.domain.transfer.valueobject.TransferId;
import com.banking.domain.transfer.valueobject.TransferStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface para o agregado Transfer.
 */
public interface TransferRepository extends Repository<Transfer, TransferId> {

    /**
     * Encontra uma transferência pela chave de idempotência
     */
    Optional<Transfer> findByIdempotencyKey(IdempotencyKey idempotencyKey);

    /**
     * Verifica se existe uma transferência com a chave de idempotência
     */
    boolean existsByIdempotencyKey(IdempotencyKey idempotencyKey);

    /**
     * Encontra transferências por conta de origem
     */
    List<Transfer> findByFromAccountId(AccountId fromAccountId);

    /**
     * Encontra transferências por conta de destino
     */
    List<Transfer> findByToAccountId(AccountId toAccountId);

    /**
     * Encontra transferências por status
     */
    List<Transfer> findByStatus(TransferStatus status);

    /**
     * Encontra histórico de transferências de uma conta (origem ou destino)
     */
    List<Transfer> findTransferHistory(AccountId accountId);
}