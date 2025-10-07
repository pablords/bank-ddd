#!/bin/bash

# Script para inicialização completa do ambiente Banking Application
# Este script configura e inicia todos os serviços necessários

set -e  # Sai se qualquer comando falhar

echo "=========================================="
echo "   Banking Application - Setup Script"
echo "=========================================="

# Cores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Função para logging
log() {
    echo -e "${GREEN}[$(date +'%Y-%m-%d %H:%M:%S')] $1${NC}"
}

warn() {
    echo -e "${YELLOW}[$(date +'%Y-%m-%d %H:%M:%S')] WARNING: $1${NC}"
}

error() {
    echo -e "${RED}[$(date +'%Y-%m-%d %H:%M:%S')] ERROR: $1${NC}"
}

# Verifica se o Docker está rodando
check_docker() {
    log "Verificando Docker..."
    if ! command -v docker &> /dev/null; then
        error "Docker não está instalado!"
        exit 1
    fi
    
    if ! docker info &> /dev/null; then
        error "Docker não está rodando!"
        exit 1
    fi
    
    log "Docker OK ✓"
}

# Verifica se o Maven está instalado
check_maven() {
    log "Verificando Maven..."
    if ! command -v mvn &> /dev/null; then
        error "Maven não está instalado!"
        exit 1
    fi
    
    log "Maven OK ✓"
}

# Função para compilar o projeto
build_project() {
    log "Compilando o projeto Banking..."
    
    cd "$(dirname "$0")/.."  # Vai para o diretório raiz do projeto
    
    mvn clean install -DskipTests
    
    if [ $? -eq 0 ]; then
        log "Build concluído com sucesso ✓"
    else
        error "Falha no build!"
        exit 1
    fi
}

# Função para iniciar a infraestrutura
start_infrastructure() {
    log "Iniciando infraestrutura (PostgreSQL, Redis, RabbitMQ)..."
    
    cd banking-bootstrap
    
    docker-compose up -d postgres redis rabbitmq
    
    # Aguarda os serviços ficarem prontos
    log "Aguardando PostgreSQL ficar pronto..."
    until docker-compose exec -T postgres pg_isready -U banking -d banking; do
        sleep 2
    done
    
    log "Aguardando Redis ficar pronto..."
    until docker-compose exec -T redis redis-cli ping; do
        sleep 2
    done
    
    log "Aguardando RabbitMQ ficar pronto..."
    until docker-compose exec -T rabbitmq rabbitmq-diagnostics -q ping; do
        sleep 2
    done
    
    log "Infraestrutura iniciada com sucesso ✓"
}

# Função para executar migrações do banco
run_migrations() {
    log "Executando migrações do banco de dados..."
    
    cd ..  # Volta para o diretório raiz
    mvn flyway:migrate -pl banking-bootstrap
    
    if [ $? -eq 0 ]; then
        log "Migrações executadas com sucesso ✓"
    else
        warn "Erro nas migrações - prosseguindo mesmo assim"
    fi
}

# Função para iniciar a aplicação
start_application() {
    log "Iniciando Banking Application..."
    
    cd banking-bootstrap
    
    # Opção 1: Rodar via Docker
    if [ "$1" = "docker" ]; then
        log "Iniciando aplicação via Docker..."
        docker-compose --profile full-stack up -d banking-app
    else
        # Opção 2: Rodar localmente via Maven
        log "Iniciando aplicação localmente..."
        mvn spring-boot:run &
        APP_PID=$!
        echo $APP_PID > app.pid
        log "Aplicação iniciada com PID: $APP_PID"
    fi
}

# Função para verificar se a aplicação está rodando
check_application() {
    log "Verificando se a aplicação está funcionando..."
    
    # Aguarda a aplicação ficar pronta
    for i in {1..30}; do
        if curl -s http://localhost:8080/actuator/health > /dev/null; then
            log "Aplicação está funcionando ✓"
            return 0
        fi
        sleep 5
    done
    
    warn "Aplicação pode não estar funcionando corretamente"
    return 1
}

# Função para mostrar informações úteis
show_info() {
    echo ""
    echo -e "${BLUE}=========================================="
    echo "   Banking Application - Informações"
    echo -e "==========================================${NC}"
    echo ""
    echo "🌐 Aplicação Principal:"
    echo "   http://localhost:8080"
    echo ""
    echo "📚 Swagger UI (Documentação da API):"
    echo "   http://localhost:8080/swagger-ui.html"
    echo ""
    echo "🏥 Health Check:"
    echo "   http://localhost:8080/actuator/health"
    echo ""
    echo "📊 Métricas:"
    echo "   http://localhost:8080/actuator/metrics"
    echo ""
    echo "🐰 RabbitMQ Management:"
    echo "   http://localhost:15672 (guest/guest)"
    echo ""
    echo "🔧 Endpoints da API:"
    echo "   POST /api/v1/accounts - Criar conta"
    echo "   GET  /api/v1/accounts - Listar contas"
    echo "   POST /api/v1/transfers - Fazer transferência"
    echo "   GET  /api/v1/transfers/history/{accountId} - Histórico"
    echo ""
    echo -e "${GREEN}Banking Application está pronto para uso! 🚀${NC}"
}

# Função para parar tudo
stop_all() {
    log "Parando todos os serviços..."
    
    cd banking-bootstrap
    
    # Para a aplicação local se estiver rodando
    if [ -f app.pid ]; then
        kill $(cat app.pid) 2>/dev/null || true
        rm app.pid
    fi
    
    # Para os containers
    docker-compose down
    
    log "Todos os serviços foram parados ✓"
}

# Menu principal
main() {
    case "${1:-}" in
        "start")
            check_docker
            check_maven
            build_project
            start_infrastructure
            run_migrations
            start_application "${2:-}"
            check_application
            show_info
            ;;
        "stop")
            stop_all
            ;;
        "restart")
            stop_all
            sleep 3
            main start "${2:-}"
            ;;
        "build")
            check_maven
            build_project
            ;;
        "infrastructure")
            check_docker
            start_infrastructure
            ;;
        *)
            echo "Uso: $0 {start|stop|restart|build|infrastructure} [docker]"
            echo ""
            echo "Comandos:"
            echo "  start         - Inicia todo o ambiente Banking"
            echo "  start docker  - Inicia usando containers Docker"
            echo "  stop          - Para todos os serviços"
            echo "  restart       - Reinicia o ambiente"
            echo "  build         - Apenas compila o projeto"
            echo "  infrastructure - Apenas inicia a infraestrutura"
            echo ""
            echo "Exemplos:"
            echo "  $0 start        # Inicia localmente"
            echo "  $0 start docker # Inicia com Docker"
            echo "  $0 stop         # Para tudo"
            exit 1
            ;;
    esac
}

# Executa o comando principal
main "$@"