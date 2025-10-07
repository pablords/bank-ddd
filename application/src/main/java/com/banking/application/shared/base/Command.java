package com.banking.application.shared.base;

/**
 * Interface base para todos os Commands do sistema.
 * Commands representam intenções de mudança de estado.
 */
public interface Command {
    
    /**
     * Retorna um identificador único para este command.
     * Útil para logging, auditoria e correlação.
     */
    default String getCommandId() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * Retorna o timestamp de criação do command.
     */
    default java.time.LocalDateTime getCreatedAt() {
        return java.time.LocalDateTime.now();
    }

    /**
     * Retorna metadados do command para auditoria.
     */
    default java.util.Map<String, Object> getMetadata() {
        java.util.Map<String, Object> metadata = new java.util.HashMap<>();
        metadata.put("commandId", getCommandId());
        metadata.put("commandType", this.getClass().getSimpleName());
        metadata.put("createdAt", getCreatedAt());
        return metadata;
    }
}