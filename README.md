# Banking Application - Sistema Bancário Acadêmico DDD

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Redis](https://img.shields.io/badge/Redis-7-red)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3-orange)
![Docker](https://img.shields.io/badge/Docker-Compose-blue)

Sistema bancário acadêmico desenvolvido para validar conceitos de **ACID**, **idempotência**, **transações**, **Domain-Driven Design (DDD)** e **arquitetura orientada a eventos**.

## 🏗️ Arquitetura

Aplicação implementada seguindo os princípios de **Domain-Driven Design (DDD)** com separação clara de responsabilidades:

### 📦 Módulos

- **`banking-domain`** - Lógica de negócio pura (Entities, Value Objects, Domain Services)
- **`banking-application`** - Casos de uso e orquestração (Commands, Queries, Handlers)
- **`banking-infrastructure`** - Detalhes técnicos (Persistência, Cache, Mensageria)
- **`banking-interfaces`** - Pontos de entrada (REST API, CLI)
- **`banking-bootstrap`** - Configuração e inicialização da aplicação

### 🔄 Dependências entre Módulos

```
banking-bootstrap
    ├── banking-interfaces
    ├── banking-infrastructure  
    ├── banking-application
    └── banking-domain

banking-interfaces
    ├── banking-application
    └── banking-domain

banking-infrastructure
    ├── banking-application
    └── banking-domain

banking-application
    └── banking-domain

banking-domain
    └── (sem dependências externas)
```

## 🎯 Conceitos Validados

### ACID Properties
- **Atomicidade**: Transações completas ou rollback total
- **Consistência**: Regras de negócio sempre mantidas
- **Isolamento**: Prevenção de dirty reads e phantom reads
- **Durabilidade**: Persistência garantida após commit

### Outros Conceitos
- **Idempotência**: Operações seguras para retry
- **Domain-Driven Design**: Modelagem rica do domínio
- **CQRS**: Separação de Commands e Queries
- **Event Sourcing**: Histórico completo de eventos
- **Transações Distribuídas**: Saga Pattern e 2PC

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.8+
- Docker e Docker Compose
- PostgreSQL (via Docker)
- Redis (via Docker)
- RabbitMQ (via Docker)

### Executar Localmente

1. **Iniciar dependências com Docker**
```bash
docker-compose up -d postgres redis rabbitmq
```

2. **Compilar todos os módulos**
```bash
mvn clean compile
```

3. **Executar testes**
```bash
mvn test
```

4. **Executar a aplicação**
```bash
cd banking-bootstrap
mvn spring-boot:run
```

### API Endpoints

- **Health Check**: `GET /api/v1/health`
- **Criar Conta**: `POST /api/v1/accounts`
- **Consultar Conta**: `GET /api/v1/accounts/{id}`
- **Consultar Saldo**: `GET /api/v1/accounts/{id}/balance`
- **Processar Transferência**: `POST /api/v1/transfers`
- **Consultar Transferência**: `GET /api/v1/transfers/{id}`
- **Histórico de Transferências**: `GET /api/v1/accounts/{id}/transfers`

### Documentação da API

Acesse: http://localhost:8080/swagger-ui.html

## 🧪 Testes

### Executar todos os testes
```bash
mvn test
```

### Testes por módulo
```bash
# Testes de domínio
cd banking-domain && mvn test

# Testes de aplicação  
cd banking-application && mvn test

# Testes de infraestrutura
cd banking-infrastructure && mvn test

# Testes de interface
cd banking-interfaces && mvn test

# Testes de integração
cd banking-bootstrap && mvn test
```

### Testes E2E
```bash
mvn verify
```

## 📊 Monitoramento

- **Métricas**: http://localhost:8080/actuator/metrics
- **Health**: http://localhost:8080/actuator/health
- **Prometheus**: http://localhost:8080/actuator/prometheus

## 🔧 Configuração

### Profiles Disponíveis
- **dev**: Desenvolvimento local
- **test**: Execução de testes
- **prod**: Produção

### Variáveis de Ambiente
```bash
# Database
DATABASE_URL=jdbc:postgresql://localhost:5432/banking
DATABASE_USERNAME=banking_user
DATABASE_PASSWORD=banking_pass

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# RabbitMQ
RABBITMQ_HOST=localhost
RABBITMQ_PORT=5672
RABBITMQ_USERNAME=guest
RABBITMQ_PASSWORD=guest
```

## 📚 Documentação Adicional

- [Requisitos do Sistema](REQUISITOS_SISTEMA_BANCARIO.md)
- [Arquitetura DDD](docs/architecture/ddd-design.md)
- [Padrões de Transação](docs/architecture/transaction-patterns.md)
- [Bounded Contexts](docs/architecture/bounded-contexts.md)

## 🤝 Contribuição

Este é um projeto acadêmico para demonstração de conceitos. Contribuições são bem-vindas!

## 📄 Licença

MIT License - veja o arquivo [LICENSE](LICENSE) para detalhes.