package com.itcycle.whatsapp.domain.port.out;

import com.itcycle.whatsapp.domain.event.IncomingMessageEvent;

/**
 * EventPublisherPort - output port for publishing domain events.
 * This interface defines the contract for publishing events that can trigger
 * automations, notifications, or other async processing.
 */
public interface EventPublisherPort {
    
    /**
     * Publish an incoming message event
     * @param event the domain event to publish
     */
    void publishIncomingMessage(IncomingMessageEvent event);
}
