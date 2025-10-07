package com.banking.application.shared.base;

/**
 * Interface base para todos os Query Handlers do sistema.
 * Implementa o padrão Query Handler para processar consultas.
 */
public interface QueryHandler<TQuery extends Query<TResult>, TResult> {

    /**
     * Processa a query e retorna o resultado.
     * 
     * @param query A query a ser processada
     * @return O resultado da consulta
     * @throws Exception Se ocorrer erro durante o processamento
     */
    TResult handle(TQuery query) throws Exception;

    /**
     * Retorna o tipo de query que este handler processa.
     */
    default Class<TQuery> getQueryType() {
        // Este método pode ser sobrescrito se necessário
        return null;
    }

    /**
     * Indica se este handler suporta a query especificada.
     */
    default boolean canHandle(Query<?> query) {
        Class<TQuery> queryType = getQueryType();
        return queryType != null && queryType.isAssignableFrom(query.getClass());
    }

    /**
     * Executa validações pré-processamento da query.
     * Pode ser sobrescrito para validações específicas.
     */
    default void validate(TQuery query) {
        if (query == null) {
            throw new IllegalArgumentException("Query cannot be null");
        }
    }

    /**
     * Indica se o resultado desta query deve ser cacheado.
     */
    default boolean shouldCache(TQuery query) {
        return query.isCacheable();
    }

    /**
     * Retorna a chave de cache para o resultado desta query.
     */
    default String getCacheKey(TQuery query) {
        return query.getCacheKey();
    }
}