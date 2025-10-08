package com.banking.bootstrap.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configuração principal que organiza as configurações dos módulos.
 * As configurações são descobertas automaticamente via ComponentScan.
 */
@Configuration
public class BankingConfig {
    // As configurações são descobertas automaticamente via @ComponentScan
    // definido na classe principal BankingApplication
}