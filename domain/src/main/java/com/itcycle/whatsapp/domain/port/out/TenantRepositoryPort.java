package com.itcycle.whatsapp.domain.port.out;

import java.util.Optional;
import java.util.UUID;

import com.itcycle.whatsapp.domain.model.Tenant;

/**
 * TenantRepositoryPort - output port for tenant persistence operations.
 * This interface defines the contract for storing and retrieving tenants.
 * Implementations are provided by the adapters-out-persistence module.
 */
public interface TenantRepositoryPort {
    
    Tenant save(Tenant tenant);
    
    Optional<Tenant> findById(UUID id);
    
    Optional<Tenant> findByApiKey(String apiKey);
    
    Optional<Tenant> findByWhatsappPhoneNumberId(String whatsappPhoneNumberId);
}
