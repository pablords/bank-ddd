package com.banking.domain.shared.interfaces;

import java.util.List;
import java.util.Optional;

/**
 * Interface base para todos os repositórios do domínio.
 * Define operações básicas de persistência.
 */
public interface Repository<T, ID> {

    /**
     * Salva ou atualiza uma entidade
     */
    T save(T entity);

    /**
     * Encontra uma entidade por ID
     */
    Optional<T> findById(ID id);

    /**
     * Verifica se uma entidade existe por ID
     */
    boolean existsById(ID id);

    /**
     * Remove uma entidade por ID
     */
    void deleteById(ID id);

    /**
     * Remove uma entidade
     */
    void delete(T entity);

    /**
     * Retorna todas as entidades
     */
    List<T> findAll();

    /**
     * Conta o número total de entidades
     */
    long count();
}