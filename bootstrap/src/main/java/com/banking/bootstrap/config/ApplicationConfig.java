package com.banking.bootstrap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Configuração geral da aplicação.
 * Define beans e configurações específicas do contexto da aplicação.
 */
@Configuration
@EnableAsync
@EnableScheduling
public class ApplicationConfig {

    /**
     * Configuração do executor de tarefas assíncronas
     */
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("Async-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(30);
        executor.initialize();
        return executor;
    }

    /**
     * Configuração específica para ambiente de desenvolvimento
     */
    @Configuration
    @Profile("dev")
    static class DevelopmentConfig {
        
        @Bean
        public String developmentMessage() {
            System.out.println("=== AMBIENTE DE DESENVOLVIMENTO ===");
            System.out.println("- Logs detalhados habilitados");
            System.out.println("- DevTools ativo para hot reload");
            System.out.println("- H2 Console disponível (se configurado)");
            System.out.println("===================================");
            return "Development mode active";
        }
    }

    /**
     * Configuração específica para ambiente de produção
     */
    @Configuration
    @Profile("prod")
    static class ProductionConfig {
        
        @Bean
        public String productionMessage() {
            System.out.println("=== AMBIENTE DE PRODUÇÃO ===");
            System.out.println("- Logs otimizados");
            System.out.println("- Métricas de performance ativas");
            System.out.println("- Health checks configurados");
            System.out.println("============================");
            return "Production mode active";
        }
    }

    /**
     * Configuração específica para testes
     */
    @Configuration
    @Profile("test")
    static class TestConfig {
        
        @Bean
        public String testMessage() {
            System.out.println("=== AMBIENTE DE TESTE ===");
            System.out.println("- Testcontainers ativos");
            System.out.println("- Banco de dados em memória");
            System.out.println("- Mensageria mockada");
            System.out.println("=========================");
            return "Test mode active";
        }
    }
}