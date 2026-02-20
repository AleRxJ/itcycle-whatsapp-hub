package com.itcycle.whatsapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

/**
 * Tenant - represents a company/organization using the WhatsApp Hub platform.
 * Domain entity following DDD principles.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {
    private UUID id;
    private String companyName;
    private String whatsappBusinessAccountId;
    private String whatsappPhoneNumberId;
    private String apiKey;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
