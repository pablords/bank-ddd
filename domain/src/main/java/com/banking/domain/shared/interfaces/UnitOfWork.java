package com.banking.domain.shared.interfaces;

/**
 * Interface para Unit of Work pattern.
 * Gerencia transações e coordena mudanças em múltiplos agregados.
 */
public interface UnitOfWork {

    /**
     * Inicia uma nova unidade de trabalho
     */
    void begin();

    /**
     * Confirma todas as mudanças
     */
    void commit();

    /**
     * Desfaz todas as mudanças
     */
    void rollback();

    /**
     * Verifica se há uma transação ativa
     */
    boolean isActive();

    /**
     * Executa uma operação dentro de uma transação
     */
    <T> T executeInTransaction(TransactionalOperation<T> operation);

    /**
     * Interface funcional para operações transacionais
     */
    @FunctionalInterface
    interface TransactionalOperation<T> {
        T execute() throws Exception;
    }
}