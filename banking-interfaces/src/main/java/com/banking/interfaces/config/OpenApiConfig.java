package com.banking.interfaces.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do OpenAPI/Swagger para documentação da API.
 * Define metadados e configurações da documentação automática.
 */
@Configuration
public class OpenApiConfig {

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.name:Banking Application}")
    private String appName;

    @Value("${app.description:Sistema bancário acadêmico para validação de conceitos ACID}")
    private String appDescription;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(appName)
                        .description(appDescription)
                        .version(appVersion)
                        .contact(new Contact()
                                .name("Banking Team")
                                .email("banking@example.com")
                                .url("https://github.com/banking-team/banking-app"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080" + contextPath)
                                .description("Servidor de desenvolvimento"),
                        new Server()
                                .url("https://banking-api.example.com" + contextPath)
                                .description("Servidor de produção")
                ));
    }
}