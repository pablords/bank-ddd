package com.banking.infrastructure.messaging.listener;

import com.banking.domain.event.AccountCreatedEvent;
import com.banking.domain.event.TransferCompletedEvent;
import com.banking.domain.event.TransferFailedEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Listeners para eventos de domínio usando RabbitMQ.
 * Processa eventos assíncronos para operações complementares.
 */
@Component
public class DomainEventListener {

    /**
     * Processa eventos de conta criada
     */
    @RabbitListener(queues = "${banking.messaging.queues.account-created:banking.account.created}")
    public void handleAccountCreated(AccountCreatedEvent event) {
        try {
            System.out.println("=== EVENTO: Conta Criada ===");
            System.out.println("Account ID: " + event.getAccountId());
            System.out.println("Account Number: " + event.getAccountNumber());
            System.out.println("Holder Name: " + event.getHolderName());
            System.out.println("Timestamp: " + event.getTimestamp());
            
            // Aqui você pode implementar lógicas complementares como:
            // - Envio de email de boas-vindas
            // - Criação de registros de auditoria
            // - Integração com sistemas externos
            // - Atualização de caches
            
            processAccountCreatedSideEffects(event);
            
        } catch (Exception e) {
            System.err.println("Erro ao processar evento de conta criada: " + e.getMessage());
            // Em produção, implementar retry logic e dead letter queue
        }
    }

    /**
     * Processa eventos de transferência completada
     */
    @RabbitListener(queues = "${banking.messaging.queues.transfer-completed:banking.transfer.completed}")
    public void handleTransferCompleted(TransferCompletedEvent event) {
        try {
            System.out.println("=== EVENTO: Transferência Completada ===");
            System.out.println("Transfer ID: " + event.getTransferId());
            System.out.println("From Account: " + event.getFromAccountId());
            System.out.println("To Account: " + event.getToAccountId());
            System.out.println("Amount: " + event.getAmount());
            System.out.println("Timestamp: " + event.getTimestamp());
            
            // Lógicas complementares para transferência completada:
            // - Notificações push/email para ambas as contas
            // - Atualização de relatórios em tempo real
            // - Sincronização com sistemas de contabilidade
            // - Cálculo de cashback/recompensas
            
            processTransferCompletedSideEffects(event);
            
        } catch (Exception e) {
            System.err.println("Erro ao processar evento de transferência completada: " + e.getMessage());
        }
    }

    /**
     * Processa eventos de transferência falhada
     */
    @RabbitListener(queues = "${banking.messaging.queues.transfer-failed:banking.transfer.failed}")
    public void handleTransferFailed(TransferFailedEvent event) {
        try {
            System.out.println("=== EVENTO: Transferência Falhada ===");
            System.out.println("Transfer ID: " + event.getTransferId());
            System.out.println("From Account: " + event.getFromAccountId());
            System.out.println("To Account: " + event.getToAccountId());
            System.out.println("Amount: " + event.getAmount());
            System.out.println("Reason: " + event.getReason());
            System.out.println("Timestamp: " + event.getTimestamp());
            
            // Lógicas para transferência falhada:
            // - Notificação de falha para o usuário
            // - Log detalhado para análise
            // - Potencial revert de operações relacionadas
            // - Alertas para equipe de suporte
            
            processTransferFailedSideEffects(event);
            
        } catch (Exception e) {
            System.err.println("Erro ao processar evento de transferência falhada: " + e.getMessage());
        }
    }

    /**
     * Processa efeitos colaterais da criação de conta
     */
    private void processAccountCreatedSideEffects(AccountCreatedEvent event) {
        // Implementar lógicas específicas do negócio
        System.out.println("Processando efeitos colaterais da criação de conta...");
        
        // Exemplo: Cache warming
        // cacheService.put("account:" + event.getAccountId(), event, Duration.ofHours(1));
        
        // Exemplo: Auditoria
        // auditService.logAccountCreation(event);
        
        // Exemplo: Integração externa
        // externalService.notifyAccountCreation(event);
    }

    /**
     * Processa efeitos colaterais da transferência completada
     */
    private void processTransferCompletedSideEffects(TransferCompletedEvent event) {
        System.out.println("Processando efeitos colaterais da transferência completada...");
        
        // Exemplo: Atualização de estatísticas
        // statisticsService.updateTransferStats(event);
        
        // Exemplo: Notificações
        // notificationService.sendTransferCompletedNotification(event);
    }

    /**
     * Processa efeitos colaterais da transferência falhada
     */
    private void processTransferFailedSideEffects(TransferFailedEvent event) {
        System.out.println("Processando efeitos colaterais da transferência falhada...");
        
        // Exemplo: Alertas
        // alertService.sendTransferFailureAlert(event);
        
        // Exemplo: Métricas
        // metricsService.incrementFailureCounter(event.getReason());
    }
}