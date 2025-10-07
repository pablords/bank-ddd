package com.banking.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;

/**
 * Configuração do JPA e DataSource.
 * Define conexão com banco de dados e configurações de persistência.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.banking.infrastructure.persistence.jpa.repository")
@EnableJpaAuditing
@EnableTransactionManagement
public class JpaConfig {

    @Value("${spring.datasource.url:jdbc:postgresql://localhost:5432/banking}")
    private String datasourceUrl;

    @Value("${spring.datasource.username:banking}")
    private String datasourceUsername;

    @Value("${spring.datasource.password:banking}")
    private String datasourcePassword;

    @Value("${spring.datasource.driver-class-name:org.postgresql.Driver}")
    private String driverClassName;

    /**
     * Configuração do DataSource
     */
    @Bean
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .url(datasourceUrl)
                .username(datasourceUsername)
                .password(datasourcePassword)
                .driverClassName(driverClassName)
                .build();
    }
}