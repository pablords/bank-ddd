package com.banking.domain.shared.base;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Classe base abstrata para todos os Domain Events.
 * Domain Events representam algo importante que aconteceu no domínio.
 */
public abstract class DomainEvent {

    private final String eventId;
    private final LocalDateTime occurredOn;
    private final String eventType;

    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
        this.eventType = this.getClass().getSimpleName();
    }

    public String getEventId() {
        return eventId;
    }

    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }

    public String getEventType() {
        return eventType;
    }

    /**
     * Retorna dados específicos do evento para serialização
     */
    public abstract Object getEventData();

    /**
     * Retorna o ID do agregado que gerou o evento
     */
    public abstract String getAggregateId();

    /**
     * Retorna o tipo do agregado que gerou o evento
     */
    public abstract String getAggregateType();

    @Override
    public String toString() {
        return String.format("%s{eventId='%s', occurredOn=%s, aggregateId='%s', aggregateType='%s'}", 
            eventType, eventId, occurredOn, getAggregateId(), getAggregateType());
    }
}