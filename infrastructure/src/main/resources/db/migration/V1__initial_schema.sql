-- Criação do schema inicial do sistema bancário

-- Criação da tabela de contas
CREATE TABLE accounts (
    id VARCHAR(36) PRIMARY KEY,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    holder_name VARCHAR(100) NOT NULL,
    holder_cpf VARCHAR(11) UNIQUE NOT NULL,
    balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    is_active BOOLEAN NOT NULL DEFAULT true,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para otimização de consultas
CREATE INDEX idx_accounts_account_number ON accounts(account_number);
CREATE INDEX idx_accounts_holder_cpf ON accounts(holder_cpf);
CREATE INDEX idx_accounts_is_active ON accounts(is_active);
CREATE INDEX idx_accounts_created_at ON accounts(created_at);

-- Criação da tabela de transferências
CREATE TABLE transfers (
    id VARCHAR(36) PRIMARY KEY,
    from_account_id VARCHAR(36) NOT NULL,
    to_account_id VARCHAR(36) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    description TEXT,
    idempotency_key VARCHAR(64) UNIQUE NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_transfers_from_account FOREIGN KEY (from_account_id) REFERENCES accounts(id),
    CONSTRAINT fk_transfers_to_account FOREIGN KEY (to_account_id) REFERENCES accounts(id),
    CONSTRAINT chk_transfers_amount_positive CHECK (amount > 0),
    CONSTRAINT chk_transfers_different_accounts CHECK (from_account_id != to_account_id),
    CONSTRAINT chk_transfers_status CHECK (status IN ('PENDING', 'PROCESSING', 'COMPLETED', 'FAILED', 'CANCELLED'))
);

-- Índices para otimização de consultas de transferências
CREATE INDEX idx_transfers_from_account_id ON transfers(from_account_id);
CREATE INDEX idx_transfers_to_account_id ON transfers(to_account_id);
CREATE INDEX idx_transfers_idempotency_key ON transfers(idempotency_key);
CREATE INDEX idx_transfers_status ON transfers(status);
CREATE INDEX idx_transfers_created_at ON transfers(created_at);

-- Trigger para atualizar updated_at automaticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Aplicar trigger nas tabelas
CREATE TRIGGER update_accounts_updated_at 
    BEFORE UPDATE ON accounts 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_transfers_updated_at 
    BEFORE UPDATE ON transfers 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Inserção de dados de exemplo para testes
INSERT INTO accounts (id, account_number, holder_name, holder_cpf, balance, is_active, created_at) VALUES
('a1b2c3d4-e5f6-4789-a012-b34567890123', '12345-6', 'João Silva', '12345678901', 1000.00, true, CURRENT_TIMESTAMP),
('b2c3d4e5-f6a7-4890-b123-c45678901234', '23456-7', 'Maria Santos', '23456789012', 1500.50, true, CURRENT_TIMESTAMP),
('c3d4e5f6-a7b8-4901-c234-d56789012345', '34567-8', 'Pedro Oliveira', '34567890123', 750.25, true, CURRENT_TIMESTAMP);

-- Criação de uma view para consultas de histórico de transferências
CREATE VIEW transfer_history AS
SELECT 
    t.id,
    t.from_account_id,
    af.account_number as from_account_number,
    af.holder_name as from_holder_name,
    t.to_account_id,
    at.account_number as to_account_number,
    at.holder_name as to_holder_name,
    t.amount,
    t.description,
    t.status,
    t.created_at,
    t.updated_at
FROM transfers t
JOIN accounts af ON t.from_account_id = af.id
JOIN accounts at ON t.to_account_id = at.id
ORDER BY t.created_at DESC;

-- Comentários nas tabelas para documentação
COMMENT ON TABLE accounts IS 'Tabela de contas bancárias';
COMMENT ON TABLE transfers IS 'Tabela de transferências entre contas';
COMMENT ON VIEW transfer_history IS 'View com histórico completo de transferências incluindo dados das contas';