package com.banking.infrastructure.transaction;

import com.banking.application.shared.interfaces.TransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementação do TransactionManager usando Spring Transaction Management.
 * Integra com o PlatformTransactionManager do Spring para controle transacional.
 */
@Service
public class SpringTransactionManager implements TransactionManager {

    private final PlatformTransactionManager platformTransactionManager;

    public SpringTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }

    @Override
    @Transactional
    public <T> T executeInTransaction(TransactionalOperation<T> operation) throws Exception {
        return operation.execute();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <T> T executeInTransactionWithRollback(TransactionalOperation<T> operation) throws Exception {
        return operation.execute();
    }

    @Override
    @Transactional(readOnly = true)
    public <T> T executeInReadOnlyTransaction(TransactionalOperation<T> operation) throws Exception {
        return operation.execute();
    }

    @Override
    public TransactionContext beginTransaction() {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = platformTransactionManager.getTransaction(definition);
        return new SpringTransactionContext(status, platformTransactionManager);
    }

    @Override
    public TransactionContext getCurrentTransaction() {
        // Para obter a transação atual, precisaríamos de uma implementação mais complexa
        // que mantém um ThreadLocal com o contexto atual
        // Por simplicidade, retornamos null aqui, mas uma implementação completa
        // manteria o contexto da transação atual
        return null;
    }

    /**
     * Implementação do TransactionContext usando Spring TransactionStatus
     */
    private static class SpringTransactionContext implements TransactionContext {
        
        private final TransactionStatus transactionStatus;
        private final PlatformTransactionManager transactionManager;
        private final String transactionId;

        public SpringTransactionContext(TransactionStatus transactionStatus, 
                                       PlatformTransactionManager transactionManager) {
            this.transactionStatus = transactionStatus;
            this.transactionManager = transactionManager;
            this.transactionId = UUID.randomUUID().toString();
        }

        @Override
        public void commit() throws Exception {
            if (transactionStatus != null && !transactionStatus.isCompleted()) {
                transactionManager.commit(transactionStatus);
            }
        }

        @Override
        public void rollback() throws Exception {
            if (transactionStatus != null && !transactionStatus.isCompleted()) {
                transactionManager.rollback(transactionStatus);
            }
        }

        @Override
        public boolean isActive() {
            return transactionStatus != null && !transactionStatus.isCompleted();
        }

        @Override
        public boolean isRollbackOnly() {
            return transactionStatus != null && transactionStatus.isRollbackOnly();
        }

        @Override
        public void setRollbackOnly() {
            if (transactionStatus != null) {
                transactionStatus.setRollbackOnly();
            }
        }

        @Override
        public String getTransactionId() {
            return transactionId;
        }
    }
}