package com.banking.bootstrap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import lombok.extern.slf4j.Slf4j;

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
@EnableTransactionManagement
@Slf4j
public class BankingApplication {

    public static void main(String[] args) {
        log.info("=== Iniciando Banking Application ===");
        log.info("Arquitetura: Domain-Driven Design (DDD)");
        log.info("Módulos: Domain | Application | Infrastructure | Interfaces | Bootstrap");
        log.info("Recursos: ACID, Idempotência, Event-Driven, Cache, Mensageria");
        log.info("=====================================");

        SpringApplication.run(BankingApplication.class, args);

        log.info("=== Banking Application Iniciada ===");
        log.info("Swagger UI: http://localhost:8080/swagger-ui.html");
        log.info("Actuator: http://localhost:8080/actuator");
        log.info("API Base: http://localhost:8080/api/v1");
        log.info("=====================================");
    }
}