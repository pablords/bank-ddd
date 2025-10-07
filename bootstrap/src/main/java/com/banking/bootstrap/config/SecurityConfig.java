package com.banking.bootstrap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

/**
 * Configuração de segurança da aplicação.
 * Define políticas de segurança básicas para o sistema bancário.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configuração do filtro de segurança
     * Para este projeto acadêmico, mantemos uma configuração simples
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desabilita CSRF para APIs REST (em produção, avaliar necessidade)
            .csrf(csrf -> csrf.disable())
            
            // Configuração de autorização
            .authorizeHttpRequests(authz -> authz
                // Endpoints públicos (documentação, health checks)
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/api/v1/**").permitAll() // Para fins acadêmicos
                
                // Qualquer outra requisição requer autenticação
                .anyRequest().authenticated()
            )
            
            // Configuração de headers de segurança
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.deny())
                .contentTypeOptions(contentTypeOptions -> contentTypeOptions.and())
                .httpStrictTransportSecurity(hstsConfig -> hstsConfig
                    .maxAgeInSeconds(31536000)
                    .includeSubDomains(true)
                )
                .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            );

        return http.build();
    }
}