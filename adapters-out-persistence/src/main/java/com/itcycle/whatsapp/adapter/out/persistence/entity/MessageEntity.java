package com.itcycle.whatsapp.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * MessageEntity - JPA entity for message persistence.
 */
@Entity
@Table(name = "messages",
       indexes = {
           @Index(name = "idx_conversation", columnList = "conversation_id"),
           @Index(name = "idx_whatsapp_message_id", columnList = "whatsapp_message_id"),
           @Index(name = "idx_timestamp", columnList = "timestamp")
       })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {
    
    @Id
    private UUID id;
    
    @Column(name = "conversation_id", nullable = false)
    private UUID conversationId;
    
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
    
    @Column(name = "whatsapp_message_id", unique = true)
    private String whatsappMessageId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MessageTypeEnum type;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "direction", nullable = false)
    private MessageDirectionEnum direction;
    
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;
    
    @Column(name = "media_url")
    private String mediaUrl;
    
    @Column(name = "mime_type")
    private String mimeType;
    
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    public enum MessageTypeEnum {
        TEXT, IMAGE, AUDIO, VIDEO, DOCUMENT, LOCATION, CONTACT, STICKER, INTERACTIVE, TEMPLATE, UNKNOWN
    }
    
    public enum MessageDirectionEnum {
        INCOMING, OUTGOING
    }
}
