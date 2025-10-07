package com.banking.application.shared.interfaces;

import com.banking.domain.shared.base.DomainEvent;

/**
 * Interface para publicação de eventos na camada de aplicação.
 * Abstrai a implementação de mensageria da camada de infraestrutura.
 */
public interface EventBus {

    /**
     * Publica um evento de forma síncrona
     */
    void publish(DomainEvent event);

    /**
     * Publica múltiplos eventos de forma síncrona
     */
    void publishAll(Iterable<DomainEvent> events);

    /**
     * Publica um evento de forma assíncrona
     */
    void publishAsync(DomainEvent event);

    /**
     * Publica múltiplos eventos de forma assíncrona
     */
    void publishAllAsync(Iterable<DomainEvent> events);

    /**
     * Registra um listener para um tipo específico de evento
     */
    <T extends DomainEvent> void subscribe(Class<T> eventType, EventListener<T> listener);

    /**
     * Remove um listener registrado
     */
    <T extends DomainEvent> void unsubscribe(Class<T> eventType, EventListener<T> listener);

    /**
     * Interface funcional para listeners de eventos
     */
    @FunctionalInterface
    interface EventListener<T extends DomainEvent> {
        void handle(T event);
    }
}