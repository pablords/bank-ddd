package com.banking.domain.shared.interfaces;

import com.banking.domain.shared.base.DomainEvent;

/**
 * Interface para publicação de Domain Events.
 * Permite desacoplamento entre domínio e infraestrutura de mensageria.
 */
public interface DomainEventPublisher {

    /**
     * Publica um único domain event
     */
    void publish(DomainEvent event);

    /**
     * Publica múltiplos domain events
     */
    void publishAll(Iterable<DomainEvent> events);
}