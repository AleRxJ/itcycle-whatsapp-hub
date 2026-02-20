package com.itcycle.whatsapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

/**
 * Message - represents a single message in a conversation.
 * Domain entity following DDD principles.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private UUID id;
    private UUID conversationId;
    private UUID tenantId;
    private String whatsappMessageId;
    private MessageType type;
    private MessageDirection direction;
    private String content;
    private String mediaUrl;
    private String mimeType;
    private Instant timestamp;
    private Instant createdAt;
    
    public boolean isIncoming() {
        return direction == MessageDirection.INCOMING;
    }
    
    public boolean isOutgoing() {
        return direction == MessageDirection.OUTGOING;
    }
    
    public boolean hasMedia() {
        return mediaUrl != null && !mediaUrl.isBlank();
    }
}
