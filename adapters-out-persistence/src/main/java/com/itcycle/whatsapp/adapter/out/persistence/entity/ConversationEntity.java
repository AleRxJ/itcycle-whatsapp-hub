package com.itcycle.whatsapp.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * ConversationEntity - JPA entity for conversation persistence.
 */
@Entity
@Table(name = "conversations",
       indexes = {
           @Index(name = "idx_tenant_customer", columnList = "tenant_id,customer_id"),
           @Index(name = "idx_status", columnList = "status")
       })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationEntity {
    
    @Id
    private UUID id;
    
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
    
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ConversationStatusEnum status;
    
    @Column(name = "started_at", nullable = false)
    private Instant startedAt;
    
    @Column(name = "last_message_at")
    private Instant lastMessageAt;
    
    @Column(name = "closed_at")
    private Instant closedAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
    
    public enum ConversationStatusEnum {
        ACTIVE, CLOSED, ARCHIVED
    }
}
