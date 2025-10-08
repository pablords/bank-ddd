package com.banking.infrastructure.messaging;

import com.banking.application.shared.interfaces.EventBus;
import com.banking.domain.shared.base.DomainEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.Map;

/**
 * Implementação do EventBus usando o EventPublisher para RabbitMQ.
 * Adapta a interface da camada de aplicação para a implementação de infraestrutura.
 */
@Service
public class EventBusImpl implements EventBus {

    private final EventPublisher eventPublisher;
    private final Map<Class<? extends DomainEvent>, List<EventListener<? extends DomainEvent>>> listeners;

    public EventBusImpl(EventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
        this.listeners = new ConcurrentHashMap<>();
    }

    @Override
    public void publish(DomainEvent event) {
        // Publica via RabbitMQ
        eventPublisher.publishEvent(event);
        
        // Notifica listeners locais
        notifyLocalListeners(event);
    }

    @Override
    public void publishAll(Iterable<DomainEvent> events) {
        for (DomainEvent event : events) {
            publish(event);
        }
    }

    @Override
    @Async
    public void publishAsync(DomainEvent event) {
        publish(event);
    }

    @Override
    @Async
    public void publishAllAsync(Iterable<DomainEvent> events) {
        publishAll(events);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> void subscribe(Class<T> eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>())
                 .add((EventListener<DomainEvent>) listener);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends DomainEvent> void unsubscribe(Class<T> eventType, EventListener<T> listener) {
        List<EventListener<? extends DomainEvent>> eventListeners = listeners.get(eventType);
        if (eventListeners != null) {
            eventListeners.remove(listener);
        }
    }

    @SuppressWarnings("unchecked")
    private void notifyLocalListeners(DomainEvent event) {
        List<EventListener<? extends DomainEvent>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (EventListener<? extends DomainEvent> listener : eventListeners) {
                try {
                    ((EventListener<DomainEvent>) listener).handle(event);
                } catch (Exception e) {
                    System.err.println("Erro ao processar evento localmente: " + e.getMessage());
                    // Log do erro, mas não interrompe o processamento dos outros listeners
                }
            }
        }
    }
}