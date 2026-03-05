package com.itcycle.whatsapp.domain.event;

import java.time.Instant;
import java.util.UUID;

import com.itcycle.whatsapp.domain.valueobject.MediaMetadata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IncomingMessageEvent - domain event published when a message is received.
 * This event can trigger automations, notifications, or other business logic.
 * 
 * Enhanced with media support (audio, image, document, video) for AI processing.
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
    
    /**
     * Phone number that sent the message
     */
    private String from;
    
    /**
     * Phone number that received the message (business number)
     */
    private String to;
    
    /**
     * Customer's name (if available from WhatsApp profile)
     */
    private String customerName;
    
    /**
     * Text content of the message (for TEXT messages)
     */
    private String content;
    
    /**
     * Message type: TEXT, AUDIO, IMAGE, DOCUMENT, VIDEO, LOCATION, CONTACT, etc.
     */
    private String messageType;
    
    /**
     * Media metadata (for non-text messages)
     * Contains mediaId, mimeType, downloadUrl, caption, etc.
     */
    private MediaMetadata media;
    
    /**
     * Original WhatsApp message ID
     */
    private String whatsappMessageId;
    
    /**
     * Message timestamp from WhatsApp
     */
    private Instant timestamp;
    
    /**
     * Check if this message has media attachments
     */
    public boolean hasMedia() {
        return media != null && media.getMediaId() != null;
    }
    
    /**
     * Check if this is a text-only message
     */
    public boolean isTextOnly() {
        return "TEXT".equalsIgnoreCase(messageType) && !hasMedia();
    }
}
