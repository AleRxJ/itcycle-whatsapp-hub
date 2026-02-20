package com.itcycle.whatsapp.adapter.out.external.event;

import com.itcycle.whatsapp.domain.event.IncomingMessageEvent;
import com.itcycle.whatsapp.domain.port.out.EventPublisherPort;
import com.itcycle.whatsapp.domain.port.out.N8nClientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * RabbitEventPublisher - publishes domain events to RabbitMQ.
 * Also triggers n8n workflows for incoming messages.
 */
@Component
@ConditionalOnProperty(name = "events.rabbitmq.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
@Slf4j
public class RabbitEventPublisher implements EventPublisherPort {
    
    private final RabbitTemplate rabbitTemplate;
    private final N8nClientPort n8nClient;
    
    @Value("${rabbitmq.exchange.events:whatsapp.events}")
    private String exchange;
    
    @Value("${rabbitmq.routing.key.incoming.message:message.incoming}")
    private String incomingMessageRoutingKey;
    
    @Value("${events.rabbitmq.enabled:true}")
    private boolean rabbitEnabled;
    
    @Value("${events.n8n.trigger.enabled:true}")
    private boolean n8nTriggerEnabled;
    
    @Override
    public void publishIncomingMessage(IncomingMessageEvent event) {
        log.info("Publishing incoming message event: messageId={}", event.getMessageId());
        
        // Publish to RabbitMQ
        if (rabbitEnabled) {
            try {
                rabbitTemplate.convertAndSend(exchange, incomingMessageRoutingKey, event);
                log.debug("Event published to RabbitMQ: {}", event);
            } catch (Exception e) {
                log.error("Error publishing event to RabbitMQ", e);
            }
        }
        
        // Trigger n8n workflow
        if (n8nTriggerEnabled) {
            try {
                Map<String, Object> payload = new HashMap<>();
                payload.put("eventType", "incoming_message");
                payload.put("messageId", event.getMessageId().toString());
                payload.put("conversationId", event.getConversationId().toString());
                payload.put("customerId", event.getCustomerId().toString());
                payload.put("tenantId", event.getTenantId().toString());
                payload.put("content", event.getContent());
                payload.put("messageType", event.getMessageType());
                payload.put("timestamp", event.getTimestamp().toString());
                
                n8nClient.triggerFlow("message-received", payload);
            } catch (Exception e) {
                log.error("Error triggering n8n workflow", e);
            }
        }
    }
}
