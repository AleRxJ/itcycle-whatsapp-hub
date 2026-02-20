package com.itcycle.whatsapp.adapter.out.external.event;

import com.itcycle.whatsapp.domain.event.IncomingMessageEvent;
import com.itcycle.whatsapp.domain.port.out.EventPublisherPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Dummy event publisher for development without RabbitMQ
 */
@Component
@ConditionalOnProperty(name = "events.rabbitmq.enabled", havingValue = "false", matchIfMissing = false)
@Slf4j
public class DummyEventPublisher implements EventPublisherPort {
    
    @Override
    public void publishIncomingMessage(IncomingMessageEvent event) {
        log.info("📨 [DEV MODE] Event would be published: messageId={}, customerId={}, tenantId={}", 
                event.getMessageId(), 
                event.getCustomerId(), 
                event.getTenantId());
    }
}
