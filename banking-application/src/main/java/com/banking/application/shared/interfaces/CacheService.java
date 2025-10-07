package com.banking.application.shared.interfaces;

import java.time.Duration;
import java.util.Optional;

/**
 * Interface para serviços de cache na camada de aplicação.
 * Abstrai a implementação específica de cache (Redis, etc.).
 */
public interface CacheService {

    /**
     * Armazena um valor no cache
     */
    void put(String key, Object value);

    /**
     * Armazena um valor no cache com TTL específico
     */
    void put(String key, Object value, Duration ttl);

    /**
     * Recupera um valor do cache
     */
    <T> Optional<T> get(String key, Class<T> type);

    /**
     * Verifica se uma chave existe no cache
     */
    boolean exists(String key);

    /**
     * Remove uma chave do cache
     */
    void evict(String key);

    /**
     * Remove múltiplas chaves do cache
     */
    void evictAll(String... keys);

    /**
     * Remove todas as chaves que correspondem ao padrão
     */
    void evictByPattern(String pattern);

    /**
     * Limpa todo o cache
     */
    void clear();

    /**
     * Define TTL para uma chave existente
     */
    void expire(String key, Duration ttl);

    /**
     * Retorna o TTL restante de uma chave
     */
    Optional<Duration> getTtl(String key);

    /**
     * Incrementa um valor numérico
     */
    Long increment(String key);

    /**
     * Incrementa um valor numérico por um delta
     */
    Long increment(String key, long delta);

    /**
     * Decrementa um valor numérico
     */
    Long decrement(String key);

    /**
     * Decrementa um valor numérico por um delta
     */
    Long decrement(String key, long delta);
}