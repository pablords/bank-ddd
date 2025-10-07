package com.banking.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração do RabbitMQ para mensageria de eventos.
 * Define exchanges, filas, bindings e conversores para eventos de domínio.
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${banking.messaging.exchange:banking.events}")
    private String exchangeName;

    @Value("${banking.messaging.queues.account-created:banking.account.created}")
    private String accountCreatedQueue;

    @Value("${banking.messaging.queues.transfer-completed:banking.transfer.completed}")
    private String transferCompletedQueue;

    @Value("${banking.messaging.queues.transfer-failed:banking.transfer.failed}")
    private String transferFailedQueue;

    /**
     * Exchange principal para eventos de domínio
     */
    @Bean
    public TopicExchange bankingEventsExchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    /**
     * Fila para eventos de conta criada
     */
    @Bean
    public Queue accountCreatedQueue() {
        return new Queue(accountCreatedQueue, true);
    }

    /**
     * Fila para eventos de transferência completada
     */
    @Bean
    public Queue transferCompletedQueue() {
        return new Queue(transferCompletedQueue, true);
    }

    /**
     * Fila para eventos de transferência falhada
     */
    @Bean
    public Queue transferFailedQueue() {
        return new Queue(transferFailedQueue, true);
    }

    /**
     * Binding para eventos de conta criada
     */
    @Bean
    public Binding accountCreatedBinding() {
        return BindingBuilder
                .bind(accountCreatedQueue())
                .to(bankingEventsExchange())
                .with("account.created.event");
    }

    /**
     * Binding para eventos de transferência completada
     */
    @Bean
    public Binding transferCompletedBinding() {
        return BindingBuilder
                .bind(transferCompletedQueue())
                .to(bankingEventsExchange())
                .with("transfer.completed.event");
    }

    /**
     * Binding para eventos de transferência falhada
     */
    @Bean
    public Binding transferFailedBinding() {
        return BindingBuilder
                .bind(transferFailedQueue())
                .to(bankingEventsExchange())
                .with("transfer.failed.event");
    }

    /**
     * Conversor JSON para mensagens
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Template do RabbitMQ com conversor JSON
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    /**
     * Container factory para listeners com configurações customizadas
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        
        // Configurações de performance e confiabilidade
        factory.setConcurrentConsumers(1);
        factory.setMaxConcurrentConsumers(3);
        factory.setPrefetchCount(10);
        factory.setDefaultRequeueRejected(false);
        
        return factory;
    }

    /**
     * Fila de Dead Letter para mensagens com falha
     */
    @Bean
    public Queue deadLetterQueue() {
        return new Queue("banking.events.dlq", true);
    }

    /**
     * Exchange para Dead Letter
     */
    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange("banking.events.dlx", true, false);
    }

    /**
     * Binding para Dead Letter Queue
     */
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("#");
    }
}