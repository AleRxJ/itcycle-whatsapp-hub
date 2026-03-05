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
 * CustomerEntity - JPA entity for customer persistence.
 */
@Entity
@Table(name = "customers", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = {"tenant_id", "phone_number"}),
           @UniqueConstraint(columnNames = {"tenant_id", "whatsapp_user_id"})
       })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {
    
    @Id
    private UUID id;
    
    @Column(name = "tenant_id", nullable = false)
    private UUID tenantId;
    
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;
    
    @Column(name = "whatsapp_user_id")
    private String whatsappUserId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "email")
    private String email;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
