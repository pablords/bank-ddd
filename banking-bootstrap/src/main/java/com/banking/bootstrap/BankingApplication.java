package com.banking.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Classe principal da aplicação Banking.
 * Configura e inicializa todos os módulos da arquitetura DDD.
 */
@SpringBootApplication
@ComponentScan(basePackages = {
    "com.banking.domain",
    "com.banking.application", 
    "com.banking.infrastructure",
    "com.banking.interfaces",
    "com.banking.bootstrap"
})
@EntityScan(basePackages = "com.banking.infrastructure.persistence.jpa.entity")
@EnableJpaRepositories(basePackages = "com.banking.infrastructure.persistence.jpa.repository")
@EnableTransactionManagement
public class BankingApplication {

    public static void main(String[] args) {
        System.out.println("=== Iniciando Banking Application ===");
        System.out.println("Arquitetura: Domain-Driven Design (DDD)");
        System.out.println("Módulos: Domain | Application | Infrastructure | Interfaces | Bootstrap");
        System.out.println("Recursos: ACID, Idempotência, Event-Driven, Cache, Mensageria");
        System.out.println("=====================================");
        
        SpringApplication.run(BankingApplication.class, args);
        
        System.out.println("=== Banking Application Iniciada ===");
        System.out.println("Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("Actuator: http://localhost:8080/actuator");
        System.out.println("API Base: http://localhost:8080/api/v1");
        System.out.println("=====================================");
    }
}