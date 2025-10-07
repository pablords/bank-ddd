package com.banking.infrastructure.cache;

import com.banking.infrastructure.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Serviço especializado para gerenciamento de chaves de idempotência.
 * Garante que operações não sejam executadas mais de uma vez.
 */
@Service
public class IdempotencyService {

    private static final String IDEMPOTENCY_KEY_PREFIX = "idempotency:";
    private static final Duration DEFAULT_TTL = Duration.ofHours(24); // 24 horas
    
    private final CacheService cacheService;

    @Autowired
    public IdempotencyService(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * Registra uma chave de idempotência com resultado
     */
    public void registerKey(String idempotencyKey, Object result) {
        String fullKey = buildKey(idempotencyKey);
        cacheService.put(fullKey, result, DEFAULT_TTL);
    }

    /**
     * Registra uma chave de idempotência com resultado e TTL customizado
     */
    public void registerKey(String idempotencyKey, Object result, Duration ttl) {
        String fullKey = buildKey(idempotencyKey);
        cacheService.put(fullKey, result, ttl);
    }

    /**
     * Verifica se uma chave de idempotência já foi processada
     */
    public boolean isProcessed(String idempotencyKey) {
        String fullKey = buildKey(idempotencyKey);
        return cacheService.exists(fullKey);
    }

    /**
     * Obtém o resultado de uma operação idempotente já processada
     */
    public Object getResult(String idempotencyKey) {
        String fullKey = buildKey(idempotencyKey);
        return cacheService.get(fullKey);
    }

    /**
     * Obtém o resultado de uma operação idempotente com tipo específico
     */
    public <T> T getResult(String idempotencyKey, Class<T> type) {
        String fullKey = buildKey(idempotencyKey);
        return cacheService.get(fullKey, type);
    }

    /**
     * Tenta registrar uma chave de idempotência atomicamente
     * Retorna true se conseguiu registrar (primeira vez), false se já existia
     */
    public boolean tryRegister(String idempotencyKey) {
        String fullKey = buildKey(idempotencyKey);
        return cacheService.setIfAbsent(fullKey, "PROCESSING", DEFAULT_TTL);
    }

    /**
     * Tenta registrar uma chave de idempotência atomicamente com TTL customizado
     */
    public boolean tryRegister(String idempotencyKey, Duration ttl) {
        String fullKey = buildKey(idempotencyKey);
        return cacheService.setIfAbsent(fullKey, "PROCESSING", ttl);
    }

    /**
     * Remove uma chave de idempotência (útil para limpeza ou reprocessamento)
     */
    public boolean removeKey(String idempotencyKey) {
        String fullKey = buildKey(idempotencyKey);
        return cacheService.delete(fullKey);
    }

    /**
     * Atualiza o resultado de uma chave de idempotência já registrada
     */
    public void updateResult(String idempotencyKey, Object result) {
        String fullKey = buildKey(idempotencyKey);
        if (cacheService.exists(fullKey)) {
            cacheService.put(fullKey, result, DEFAULT_TTL);
        }
    }

    /**
     * Obtém o TTL restante de uma chave de idempotência
     */
    public long getRemainingTtl(String idempotencyKey) {
        String fullKey = buildKey(idempotencyKey);
        return cacheService.getTtl(fullKey);
    }

    /**
     * Verifica se uma operação está em processamento
     */
    public boolean isProcessing(String idempotencyKey) {
        String fullKey = buildKey(idempotencyKey);
        Object value = cacheService.get(fullKey);
        return "PROCESSING".equals(value);
    }

    /**
     * Constrói a chave completa com prefixo
     */
    private String buildKey(String idempotencyKey) {
        return IDEMPOTENCY_KEY_PREFIX + idempotencyKey;
    }
}