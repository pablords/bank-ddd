package com.banking.infrastructure.cache;

import com.banking.application.shared.interfaces.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Implementação do CacheService usando Redis para armazenamento de dados temporários.
 * Fornece operações de cache para melhorar performance e implementar idempotência.
 */
@Service
public class RedisCacheService implements CacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RedisCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void put(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
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

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<T> get(String key, Class<T> type) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null && type.isInstance(value)) {
            return Optional.of((T) value);
        }
        return Optional.empty();
    }

    @Override
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    @Override
    public void evict(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void evictAll(String... keys) {
        if (keys != null && keys.length > 0) {
            redisTemplate.delete(Set.of(keys));
        }
    }

    @Override
    public void evictByPattern(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public void clear() {
        // Em produção, esta operação deve ser usada com cuidado
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    @Override
    public void expire(String key, Duration ttl) {
        redisTemplate.expire(key, ttl);
    }

    @Override
    public Optional<Duration> getTtl(String key) {
        Long ttl = redisTemplate.getExpire(key);
        if (ttl != null && ttl > 0) {
            return Optional.of(Duration.ofSeconds(ttl));
        }
        return Optional.empty();
    }

    @Override
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    @Override
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    @Override
    public Long decrement(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    @Override
    public Long decrement(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    @Override
    public boolean setIfAbsent(String key, Object value, Duration ttl) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, value, ttl));
    }

    // Métodos adicionais específicos da implementação Redis

    /**
     * Remove uma chave do cache retornando sucesso
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
     * Define TTL para uma chave existente em segundos
     */
    public boolean expire(String key, long timeoutSeconds) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeoutSeconds, TimeUnit.SECONDS));
    }

    /**
     * Obtém o TTL de uma chave em segundos
     */
    public long getTtlInSeconds(String key) {
        Long ttl = redisTemplate.getExpire(key);
        return ttl != null ? ttl : -1;
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
        // Usa clear() que é mais seguro e não depende de métodos deprecados
        clear();
    }
}