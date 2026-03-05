package com.itcycle.whatsapp.domain.model;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Customer - represents an end-user who interacts via WhatsApp.
 * Domain entity following DDD principles.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    private UUID id;
    private UUID tenantId;
    private String phoneNumber;
    private String whatsappUserId;
    private String name;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;
}
