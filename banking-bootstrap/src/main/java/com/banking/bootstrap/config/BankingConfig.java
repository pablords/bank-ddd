package com.banking.bootstrap.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.banking.infrastructure.config.JpaConfig;
import com.banking.infrastructure.config.RedisConfig;
import com.banking.infrastructure.config.RabbitMQConfig;
import com.banking.interfaces.config.OpenApiConfig;
import com.banking.interfaces.config.WebConfig;

/**
 * Configuração principal que importa todas as configurações dos módulos.
 * Centraliza a configuração da aplicação seguindo o padrão de composição.
 */
@Configuration
@Import({
    // Configurações de infraestrutura
    JpaConfig.class,
    RedisConfig.class,
    RabbitMQConfig.class,
    
    // Configurações de interface
    OpenApiConfig.class,
    WebConfig.class,
    
    // Configurações específicas do bootstrap
    ApplicationConfig.class,
    SecurityConfig.class,
    MonitoringConfig.class
})
public class BankingConfig {
    
    // Esta classe serve como ponto central de configuração
    // Todas as configurações específicas são importadas dos módulos apropriados
}