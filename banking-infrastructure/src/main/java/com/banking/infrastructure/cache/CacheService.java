package com.banking.infrastructure.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Serviço de cache usando Redis para armazenamento de dados temporários.
 * Fornece operações de cache para melhorar performance e implementar idempotência.
 */
@Service
public class CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public CacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Armazena um valor no cache com TTL
     */
    public void put(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    /**
     * Armazena um valor no cache com TTL em segundos
     */
    public void put(String key, Object value, long timeoutSeconds) {
        redisTemplate.opsForValue().set(key, value, timeoutSeconds, TimeUnit.SECONDS);
    }

    /**
     * Recupera um valor do cache
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Recupera um valor do cache com tipo específico
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null && type.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * Verifica se uma chave existe no cache
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * Remove uma chave do cache
     */
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * Remove múltiplas chaves do cache
     */
    public long delete(String... keys) {
        Long deleted = redisTemplate.delete(Set.of(keys));
        return deleted != null ? deleted : 0;
    }

    /**
     * Define TTL para uma chave existente
     */
    public boolean expire(String key, Duration ttl) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, ttl));
    }

    /**
     * Define TTL para uma chave existente em segundos
     */
    public boolean expire(String key, long timeoutSeconds) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeoutSeconds, TimeUnit.SECONDS));
    }

    /**
     * Obtém o TTL de uma chave
     */
    public long getTtl(String key) {
        Long ttl = redisTemplate.getExpire(key);
        return ttl != null ? ttl : -1;
    }

    /**
     * Incrementa um valor numérico
     */
    public long increment(String key) {
        Long result = redisTemplate.opsForValue().increment(key);
        return result != null ? result : 0;
    }

    /**
     * Incrementa um valor numérico em uma quantidade específica
     */
    public long increment(String key, long delta) {
        Long result = redisTemplate.opsForValue().increment(key, delta);
        return result != null ? result : 0;
    }

    /**
     * Decrementa um valor numérico
     */
    public long decrement(String key) {
        Long result = redisTemplate.opsForValue().decrement(key);
        return result != null ? result : 0;
    }

    /**
     * Armazena um valor apenas se a chave não existir (operação atômica)
     */
    public boolean setIfAbsent(String key, Object value, Duration ttl) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, ttl));
    }

    /**
     * Obtém todas as chaves que correspondem a um padrão
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    /**
     * Limpa todas as chaves do banco de dados atual
     */
    public void flushDb() {
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}