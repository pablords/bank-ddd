# Multi-stage Dockerfile para Banking Application

# Stage 1: Build da aplicação
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copia os arquivos POM primeiro para cache das dependências
COPY pom.xml .
COPY parent/pom.xml parent/
COPY domain/pom.xml domain/
COPY application/pom.xml application/
COPY infrastructure/pom.xml infrastructure/
COPY interfaces/pom.xml interfaces/
COPY bootstrap/pom.xml bootstrap/

# Baixa as dependências Maven
RUN mvn dependency:go-offline -B

# Copia o código fonte
COPY domain/src domain/src
COPY application/src application/src
COPY infrastructure/src infrastructure/src
COPY interfaces/src interfaces/src
COPY bootstrap/src bootstrap/src

# Executa o build
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime da aplicação
FROM eclipse-temurin:17-jre-alpine

# Instala dependências do sistema
RUN apk add --no-cache \
    curl \
    && addgroup -S banking \
    && adduser -S banking -G banking

WORKDIR /app

# Copia o JAR da aplicação do stage anterior
COPY --from=builder /app/bootstrap/target/bootstrap-*.jar app.jar

# Define o usuário não-root
USER banking

# Expõe a porta da aplicação
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Variáveis de ambiente padrão
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SPRING_PROFILES_ACTIVE=docker

# Comando para executar a aplicação
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Labels para metadados
LABEL maintainer="Banking Team <banking@example.com>"
LABEL version="1.0.0"
LABEL description="Banking Application - Sistema bancário acadêmico DDD"