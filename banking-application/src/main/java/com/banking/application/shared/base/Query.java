package com.banking.application.shared.base;

/**
 * Interface base para todas as Queries do sistema.
 * Queries representam consultas que não alteram estado.
 */
public interface Query<TResult> {
    
    /**
     * Retorna um identificador único para esta query.
     * Útil para logging, cache e correlação.
     */
    default String getQueryId() {
        return java.util.UUID.randomUUID().toString();
    }

    /**
     * Retorna o timestamp de criação da query.
     */
    default java.time.LocalDateTime getCreatedAt() {
        return java.time.LocalDateTime.now();
    }

    /**
     * Indica se esta query pode usar cache.
     * Por padrão, queries podem ser cacheadas.
     */
    default boolean isCacheable() {
        return true;
    }

    /**
     * Retorna a chave de cache para esta query.
     * Deve ser única para cada combinação de parâmetros.
     */
    default String getCacheKey() {
        return this.getClass().getSimpleName() + "_" + this.hashCode();
    }

    /**
     * Retorna o tempo de vida do cache em segundos.
     * 0 significa que não deve ser cacheado.
     */
    default int getCacheTtlSeconds() {
        return 300; // 5 minutos por padrão
    }

    /**
     * Retorna metadados da query para logging.
     */
    default java.util.Map<String, Object> getMetadata() {
        java.util.Map<String, Object> metadata = new java.util.HashMap<>();
        metadata.put("queryId", getQueryId());
        metadata.put("queryType", this.getClass().getSimpleName());
        metadata.put("createdAt", getCreatedAt());
        metadata.put("cacheable", isCacheable());
        return metadata;
    }
}