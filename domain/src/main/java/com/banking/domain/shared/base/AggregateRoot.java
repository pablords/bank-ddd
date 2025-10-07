package com.banking.domain.shared.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe base para Aggregate Roots.
 * Gerencia Domain Events e garante consistência do agregado.
 */
public abstract class AggregateRoot<ID> extends Entity<ID> {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    protected AggregateRoot() {
        super();
    }

    protected AggregateRoot(ID id) {
        super(id);
    }

    /**
     * Adiciona um domain event para ser publicado
     */
    protected void addDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    /**
     * Remove um domain event específico
     */
    protected void removeDomainEvent(DomainEvent event) {
        this.domainEvents.remove(event);
    }

    /**
     * Retorna cópia imutável dos domain events
     */
    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * Limpa todos os domain events.
     * Deve ser chamado após a publicação dos eventos.
     */
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    /**
     * Verifica se existem domain events pendentes
     */
    public boolean hasDomainEvents() {
        return !domainEvents.isEmpty();
    }

    /**
     * Método template para validação de invariantes do agregado.
     * Deve ser implementado pelas classes filhas.
     */
    protected abstract void validateInvariants();

    /**
     * Aplica alterações e valida invariantes
     */
    protected void applyChange() {
        markAsUpdated();
        validateInvariants();
    }
}