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
 * TenantEntity - JPA entity for tenant persistence.
 */
@Entity
@Table(name = "tenants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantEntity {
    
    @Id
    private UUID id;
    
    @Column(name = "company_name", nullable = false)
    private String companyName;
    
    @Column(name = "whatsapp_business_account_id")
    private String whatsappBusinessAccountId;
    
    @Column(name = "whatsapp_phone_number_id", unique = true)
    private String whatsappPhoneNumberId;
    
    @Column(name = "api_key", unique = true, nullable = false)
    private String apiKey;
    
    @Column(name = "active", nullable = false)
    private boolean active = true;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}
