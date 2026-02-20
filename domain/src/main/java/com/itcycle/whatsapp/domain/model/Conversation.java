package com.itcycle.whatsapp.domain.model;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Conversation - represents a conversation thread between a customer and the business.
 * Domain entity following DDD principles.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
    private UUID id;
    private UUID tenantId;
    private UUID customerId;
    private ConversationStatus status;
    private Instant startedAt;
    private Instant lastMessageAt;
    private Instant closedAt;
    private Instant createdAt;
    private Instant updatedAt;
    
    public boolean isActive() {
        return status == ConversationStatus.ACTIVE;
    }
    
    public void close() {
        this.status = ConversationStatus.CLOSED;
        this.closedAt = Instant.now();
        this.updatedAt = Instant.now();
    }
    
    public void reopen() {
        this.status = ConversationStatus.ACTIVE;
        this.closedAt = null;
        this.updatedAt = Instant.now();
    }
}
