package com.banking.infrastructure.messaging;

import com.banking.domain.event.DomainEvent;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Serviço para publicação de eventos de domínio usando RabbitMQ.
 * Implementa o padrão Event-Driven Architecture para comunicação assíncrona.
 */
@Service
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${banking.messaging.exchange:banking.events}")
    private String exchangeName;

    @Autowired
    public EventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Publica um evento de domínio
     */
    public void publishEvent(DomainEvent event) {
        String routingKey = generateRoutingKey(event);
        
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
            System.out.println("Evento publicado: " + event.getClass().getSimpleName() + 
                             " com routing key: " + routingKey);
        } catch (Exception e) {
            System.err.println("Erro ao publicar evento: " + e.getMessage());
            // Em um cenário real, você poderia implementar retry logic ou dead letter queue
            throw new RuntimeException("Falha na publicação do evento", e);
        }
    }

    /**
     * Publica um evento de domínio com routing key customizada
     */
    public void publishEvent(DomainEvent event, String customRoutingKey) {
        try {
            rabbitTemplate.convertAndSend(exchangeName, customRoutingKey, event);
            System.out.println("Evento publicado: " + event.getClass().getSimpleName() + 
                             " com routing key customizada: " + customRoutingKey);
        } catch (Exception e) {
            System.err.println("Erro ao publicar evento: " + e.getMessage());
            throw new RuntimeException("Falha na publicação do evento", e);
        }
    }

    /**
     * Publica múltiplos eventos em batch
     */
    public void publishEvents(java.util.List<DomainEvent> events) {
        for (DomainEvent event : events) {
            publishEvent(event);
        }
    }

    /**
     * Gera routing key baseada no tipo do evento
     */
    private String generateRoutingKey(DomainEvent event) {
        String eventType = event.getClass().getSimpleName();
        return eventType.replaceAll("([a-z])([A-Z])", "$1.$2").toLowerCase();
    }

    /**
     * Verifica se o sistema de messaging está disponível
     */
    public boolean isMessagingAvailable() {
        try {
            // Tentativa simples de verificar a conexão
            rabbitTemplate.execute(channel -> {
                return channel.isOpen();
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}