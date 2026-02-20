package com.itcycle.whatsapp.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ configuration for event publishing.
 * Only activated when RabbitMQ is enabled.
 */
@Configuration
@ConditionalOnProperty(name = "events.rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitMQConfig {
    
    @Value("${rabbitmq.exchange.events:whatsapp.events}")
    private String eventsExchange;
    
    @Value("${rabbitmq.queue.incoming.messages:whatsapp.incoming.messages}")
    private String incomingMessagesQueue;
    
    @Value("${rabbitmq.routing.key.incoming.message:message.incoming}")
    private String incomingMessageRoutingKey;
    
    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(eventsExchange);
    }
    
    @Bean
    public Queue incomingMessagesQueue() {
        return QueueBuilder.durable(incomingMessagesQueue).build();
    }
    
    @Bean
    public Binding incomingMessagesBinding() {
        return BindingBuilder
                .bind(incomingMessagesQueue())
                .to(eventsExchange())
                .with(incomingMessageRoutingKey);
    }
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
