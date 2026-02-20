package com.itcycle.whatsapp.domain.event;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IncomingMessageEvent - domain event published when a message is received.
 * This event can trigger automations, notifications, or other business logic.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomingMessageEvent {
    private UUID messageId;
    private UUID conversationId;
    private UUID customerId;
    private UUID tenantId;
    private String content;
    private String messageType;
    private Instant timestamp;
}
