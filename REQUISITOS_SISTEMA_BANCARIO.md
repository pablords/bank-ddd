# Requisitos para Sistema de Transferências Bancárias Acadêmico

## Objetivo
Desenvolver uma aplicação bancária acadêmica que permita validar e demonstrar conceitos fundamentais como ACID, idempotência, transações, concorrência e outros princípios de sistemas distribuídos.

## 1. REQUISITOS FUNCIONAIS

### 1.1 Gestão de Contas
- **RF001**: O sistema deve permitir criar contas bancárias com número único, nome do titular, CPF e saldo inicial
- **RF002**: O sistema deve permitir consultar informações básicas de uma conta (número, titular, saldo)
- **RF003**: O sistema deve permitir listar todas as contas do sistema
- **RF004**: O sistema deve validar CPF e não permitir contas duplicadas para o mesmo CPF

### 1.2 Operações de Transferência
- **RF005**: O sistema deve permitir transferências entre contas informando:
  - Conta origem
  - Conta destino  
  - Valor da transferência
  - Descrição/motivo (opcional)
- **RF006**: O sistema deve validar se a conta origem possui saldo suficiente
- **RF007**: O sistema deve validar se as contas origem e destino existem
- **RF008**: O sistema deve gerar um ID único para cada transação
- **RF009**: O sistema deve registrar timestamp de cada operação
- **RF010**: O sistema deve permitir transferências com valor mínimo de R$ 0,01

### 1.3 Histórico e Auditoria
- **RF011**: O sistema deve manter histórico completo de todas as transações
- **RF012**: O sistema deve permitir consultar extrato por conta em período específico
- **RF013**: O sistema deve permitir consultar uma transação específica pelo ID
- **RF014**: O sistema deve registrar todas as tentativas de operação (sucesso e falha)

### 1.4 Operações de Consulta
- **RF015**: O sistema deve permitir consultar saldo atual de uma conta
- **RF016**: O sistema deve permitir consultar últimas N transações de uma conta
- **RF017**: O sistema deve calcular e exibir saldo disponível em tempo real

## 2. REQUISITOS NÃO-FUNCIONAIS

### 2.1 Performance
- **RNF001**: O sistema deve processar transferências em menos de 2 segundos
- **RNF002**: O sistema deve suportar pelo menos 100 operações simultâneas
- **RNF003**: Consultas de saldo devem retornar em menos de 500ms

### 2.2 Disponibilidade
- **RNF004**: O sistema deve ter disponibilidade de 99.9% durante o período de testes
- **RNF005**: O sistema deve ter mecanismo de recuperação automática em caso de falha

### 2.3 Consistência e Integridade
- **RNF006**: O sistema NUNCA deve permitir saldo negativo (exceto autorização explícita)
- **RNF007**: A soma total dos saldos deve ser sempre conservada (princípio da conservação)
- **RNF008**: Não deve haver perda de dados em caso de falha do sistema
- **RNF009**: O sistema deve garantir que uma transferência só seja processada uma vez

### 2.4 Segurança
- **RNF010**: Todas as operações devem ser logadas com timestamp e identificação
- **RNF011**: O sistema deve validar todos os inputs para prevenir ataques de injeção
- **RNF012**: Senhas/tokens devem ser armazenados de forma criptografada

### 2.5 Monitoramento
- **RNF013**: O sistema deve gerar métricas de performance em tempo real
- **RNF014**: O sistema deve alertar sobre operações suspeitas ou falhas
- **RNF015**: Logs devem ser estruturados e facilmente consultáveis

## 3. CENÁRIOS PARA VALIDAÇÃO DE CONCEITOS ACID

### 3.1 Atomicidade (A)
**Cenário A1 - Transferência com Falha no Meio**
- **Descrição**: Simular falha durante uma transferência para verificar se a operação é completamente desfeita
- **Teste**: Iniciar transferência de R$ 100,00 da Conta A para Conta B, forçar falha após debitar da Conta A mas antes de creditar na Conta B
- **Resultado Esperado**: Ambas as contas devem manter seus saldos originais (rollback completo)

**Cenário A2 - Múltiplas Operações em Transação**
- **Descrição**: Executar múltiplas operações como uma única transação atômica
- **Teste**: Em uma única transação, fazer 3 transferências diferentes e forçar falha na terceira
- **Resultado Esperado**: Todas as 3 operações devem ser desfeitas

### 3.2 Consistência (C)
**Cenário C1 - Validação de Regras de Negócio**
- **Descrição**: Verificar se regras de negócio são mantidas mesmo com operações concorrentes
- **Teste**: Tentar transferir valor maior que o saldo disponível em operações concorrentes
- **Resultado Esperado**: Sistema deve rejeitar operações que violem regras de negócio

**Cenário C2 - Conservação de Massa**
- **Descrição**: Verificar se a soma total dos saldos é sempre conservada
- **Teste**: Executar 100 transferências aleatórias e verificar se soma total permanece igual
- **Resultado Esperado**: Soma dos saldos antes = Soma dos saldos depois

### 3.3 Isolamento (I)
**Cenário I1 - Dirty Read Prevention**
- **Descrição**: Verificar se transações não leem dados não commitados
- **Teste**: Transação A inicia transferência, Transação B tenta ler saldo antes do commit de A
- **Resultado Esperado**: Transação B deve ler saldo anterior ao início da transação A

**Cenário I2 - Phantom Read Prevention**
- **Descrição**: Verificar se novas transferências não aparecem durante leitura de extrato
- **Teste**: Ler extrato de período específico enquanto novas transações são criadas
- **Resultado Esperado**: Resultado da consulta deve ser consistente

### 3.4 Durabilidade (D)
**Cenário D1 - Recuperação após Falha**
- **Descrição**: Verificar se dados persistem após falha do sistema
- **Teste**: Executar transferência, confirmar sucesso, simular crash, reiniciar sistema
- **Resultado Esperado**: Transferência deve estar persistida e visível após reinicialização

**Cenário D2 - Integridade dos Logs**
- **Descrição**: Verificar se logs de transação permitem recuperação completa
- **Teste**: Executar várias operações, simular falha, usar logs para recuperar estado
- **Resultado Esperado**: Estado do sistema deve ser completamente recuperável

## 4. CENÁRIOS PARA VALIDAÇÃO DE IDEMPOTÊNCIA

### 4.1 Idempotência em Transferências
**Cenário ID1 - Retry de Transferência**
- **Descrição**: Verificar se executar a mesma transferência múltiplas vezes não causa duplicação
- **Teste**: Enviar mesma requisição de transferência 5 vezes com mesmo ID de transação
- **Resultado Esperado**: Apenas uma transferência deve ser processada

**Cenário ID2 - Timeout e Retry**
- **Descrição**: Simular timeout em transferência e retry da operação
- **Teste**: Iniciar transferência, simular timeout no cliente, reenviar requisição
- **Resultado Esperado**: Se primeira operação foi processada, segunda deve ser ignorada

### 4.2 Chaves de Idempotência
**Cenário ID3 - Chave de Idempotência Personalizada**
- **Descrição**: Usar chave de idempotência fornecida pelo cliente
- **Teste**: Enviar transferências diferentes com mesma chave de idempotência
- **Resultado Esperado**: Segunda transferência deve ser rejeitada

**Cenário ID4 - Expiração de Chaves**
- **Descrição**: Verificar comportamento após expiração de chave de idempotência
- **Teste**: Enviar transferência, aguardar expiração, reenviar com mesma chave
- **Resultado Esperado**: Segunda transferência deve ser processada normalmente

## 5. ESPECIFICAÇÕES TÉCNICAS

### 5.1 Arquitetura Recomendada
- **Padrão**: Arquitetura em camadas (Layered Architecture)
- **Camadas**:
  - **Apresentação**: API REST/GraphQL
  - **Aplicação**: Serviços de negócio e orquestração
  - **Domínio**: Entidades e regras de negócio
  - **Infraestrutura**: Persistência, mensageria, logs

### 5.2 Tecnologias Sugeridas
- **Backend**: Java/Spring Boot, Node.js/Express, Python/FastAPI ou C#/.NET
- **Banco de Dados**: PostgreSQL com suporte a transações ACID
- **Cache**: Redis para otimização de consultas
- **Mensageria**: RabbitMQ ou Apache Kafka para eventos
- **Monitoramento**: Prometheus + Grafana
- **Logs**: ELK Stack (Elasticsearch, Logstash, Kibana)

### 5.3 Padrões de Implementação

#### 5.3.1 Transações Distribuídas
- **Saga Pattern**: Para operações que envolvem múltiplos serviços
- **Two-Phase Commit**: Para transações distribuídas críticas
- **Compensating Actions**: Para rollback de operações distribuídas

#### 5.3.2 Idempotência
- **Idempotency Keys**: UUID único por operação
- **Tabela de Deduplicação**: Armazenar chaves processadas com TTL
- **Status Tracking**: Rastrear status de cada operação (PENDING, SUCCESS, FAILED)

#### 5.3.3 Concorrência
- **Optimistic Locking**: Versioning nas entidades principais
- **Pessimistic Locking**: Para operações críticas de saldo
- **Event Sourcing**: Armazenar eventos ao invés de estado final

### 5.4 Estrutura do Banco de Dados

#### Tabela: accounts
```sql
CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    holder_name VARCHAR(255) NOT NULL,
    holder_cpf VARCHAR(11) UNIQUE NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW()
);
```

#### Tabela: transactions
```sql
CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    idempotency_key VARCHAR(255) UNIQUE,
    from_account_id UUID REFERENCES accounts(id),
    to_account_id UUID REFERENCES accounts(id),
    amount DECIMAL(15,2) NOT NULL,
    description TEXT,
    status VARCHAR(20) NOT NULL, -- PENDING, SUCCESS, FAILED
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    processed_at TIMESTAMP,
    failure_reason TEXT
);
```

#### Tabela: account_events (Event Sourcing)
```sql
CREATE TABLE account_events (
    id UUID PRIMARY KEY,
    account_id UUID REFERENCES accounts(id),
    event_type VARCHAR(50) NOT NULL, -- CREATED, DEBITED, CREDITED
    amount DECIMAL(15,2),
    transaction_id UUID REFERENCES transactions(id),
    metadata JSONB,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);
```

### 5.5 API Endpoints

#### 5.5.1 Gestão de Contas
```
POST /api/v1/accounts - Criar conta
GET /api/v1/accounts/{id} - Consultar conta
GET /api/v1/accounts/{id}/balance - Consultar saldo
GET /api/v1/accounts - Listar contas
```

#### 5.5.2 Transferências
```
POST /api/v1/transfers - Executar transferência
GET /api/v1/transfers/{id} - Consultar transferência
GET /api/v1/accounts/{id}/transfers - Histórico de transferências
```

#### 5.5.3 Monitoramento
```
GET /api/v1/health - Health check
GET /api/v1/metrics - Métricas do sistema
GET /api/v1/accounts/total-balance - Soma total dos saldos
```

## 6. CASOS DE TESTE PARA VALIDAÇÃO

### 6.1 Testes de Atomicidade

**Teste AT001 - Rollback em Falha de Transferência**
```
Dado: Conta A com saldo R$ 1000, Conta B com saldo R$ 500
Quando: Tentar transferir R$ 200 de A para B e simular falha após débito
Então: Conta A deve manter R$ 1000 e Conta B deve manter R$ 500
```

**Teste AT002 - Transação com Múltiplas Operações**
```
Dado: 3 contas com saldos diferentes
Quando: Executar 3 transferências em uma transação e falhar na última
Então: Todas as contas devem manter saldos originais
```

### 6.2 Testes de Consistência

**Teste CO001 - Validação de Saldo Insuficiente**
```
Dado: Conta A com saldo R$ 100
Quando: Tentar transferir R$ 150 de A para qualquer conta
Então: Operação deve ser rejeitada e saldo de A mantido
```

**Teste CO002 - Conservação da Massa Financeira**
```
Dado: Sistema com múltiplas contas e saldo total conhecido
Quando: Executar 1000 transferências aleatórias
Então: Saldo total do sistema deve permanecer inalterado
```

### 6.3 Testes de Isolamento

**Teste IS001 - Prevenção de Dirty Read**
```
Dado: Transação A iniciou transferência mas não commitou
Quando: Transação B consulta saldo da conta envolvida
Então: B deve ver saldo anterior ao início de A
```

**Teste IS002 - Serialização de Operações Concorrentes**
```
Dado: 10 transações simultâneas na mesma conta
Quando: Todas tentam transferir valores diferentes
Então: Operações devem ser serializadas e executadas sequencialmente
```

### 6.4 Testes de Durabilidade

**Teste DU001 - Persistência após Restart**
```
Dado: Transferência executada com sucesso
Quando: Sistema é reiniciado
Então: Transferência deve estar visível e persistida
```

**Teste DU002 - Recuperação via Transaction Log**
```
Dado: Sistema com várias operações e falha simulada
Quando: Sistema é restaurado usando logs de transação
Então: Estado deve ser completamente recuperado
```

### 6.5 Testes de Idempotência

**Teste ID001 - Retry de Transferência**
```
Dado: Transferência com chave de idempotência específica
Quando: Mesma requisição é enviada 5 vezes
Então: Apenas uma transferência deve ser processada
```

**Teste ID002 - Timeout e Reenvio**
```
Dado: Cliente envia transferência e recebe timeout
Quando: Cliente reenvia mesma requisição
Então: Se primeira foi processada, segunda deve ser ignorada
```

### 6.6 Testes de Performance

**Teste PE001 - Throughput de Transferências**
```
Objetivo: Medir número máximo de transferências por segundo
Teste: Executar transferências concorrentes por 1 minuto
Meta: Mínimo 100 transferências/segundo
```

**Teste PE002 - Latência de Consultas**
```
Objetivo: Medir tempo de resposta de consultas de saldo
Teste: Executar 1000 consultas e medir tempo médio
Meta: Máximo 500ms por consulta
```

### 6.7 Testes de Estresse

**Teste ST001 - Concorrência Extrema**
```
Objetivo: Validar comportamento com alta concorrência
Teste: 1000 transferências simultâneas na mesma conta
Resultado: Sistema deve manter consistência
```

**Teste ST002 - Volume de Dados**
```
Objetivo: Validar performance com grande volume
Teste: Sistema com 1 milhão de contas e transferências
Resultado: Performance deve se manter aceitável
```

## 7. MÉTRICAS DE SUCESSO

### 7.1 Funcionais
- ✅ 100% das transferências válidas processadas com sucesso
- ✅ 0% de transferências duplicadas (idempotência)
- ✅ 100% de conservação da massa financeira
- ✅ 0% de violações de regras de negócio

### 7.2 Não-Funcionais
- ✅ 99.9% de disponibilidade
- ✅ < 2s para processamento de transferências
- ✅ < 500ms para consultas de saldo
- ✅ Suporte a 100+ operações simultâneas

### 7.3 ACID
- ✅ 100% de atomicidade em operações
- ✅ 100% de consistência mantida
- ✅ 0% de dirty reads ou phantom reads
- ✅ 100% de durabilidade garantida

Este documento serve como base para desenvolvimento de uma aplicação bancária acadêmica completa que permite validar todos os conceitos fundamentais de sistemas transacionais críticos.