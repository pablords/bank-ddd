package com.banking.application.shared.interfaces;

/**
 * Interface para gerenciamento de transações na camada de aplicação.
 * Abstrai a implementação específica de gerenciamento transacional.
 */
public interface TransactionManager {

    /**
     * Executa uma operação dentro de uma transação
     */
    <T> T executeInTransaction(TransactionalOperation<T> operation) throws Exception;

    /**
     * Executa uma operação dentro de uma transação com rollback automático
     */
    <T> T executeInTransactionWithRollback(TransactionalOperation<T> operation) throws Exception;

    /**
     * Executa uma operação dentro de uma transação apenas para leitura
     */
    <T> T executeInReadOnlyTransaction(TransactionalOperation<T> operation) throws Exception;

    /**
     * Inicia uma nova transação
     */
    TransactionContext beginTransaction();

    /**
     * Obtém a transação atual, se existir
     */
    TransactionContext getCurrentTransaction();

    /**
     * Interface funcional para operações transacionais
     */
    @FunctionalInterface
    interface TransactionalOperation<T> {
        T execute() throws Exception;
    }

    /**
     * Interface que representa o contexto de uma transação
     */
    interface TransactionContext {
        
        /**
         * Confirma a transação
         */
        void commit() throws Exception;

        /**
         * Desfaz a transação
         */
        void rollback() throws Exception;

        /**
         * Verifica se a transação está ativa
         */
        boolean isActive();

        /**
         * Verifica se a transação foi marcada para rollback
         */
        boolean isRollbackOnly();

        /**
         * Marca a transação para rollback
         */
        void setRollbackOnly();

        /**
         * Retorna o ID da transação
         */
        String getTransactionId();
    }
}