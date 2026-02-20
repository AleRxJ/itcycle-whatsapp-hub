package com.itcycle.whatsapp.domain.port.out;

import com.itcycle.whatsapp.domain.model.Customer;
import java.util.Optional;
import java.util.UUID;

/**
 * CustomerRepositoryPort - output port for customer persistence operations.
 * This interface defines the contract for storing and retrieving customers.
 * Implementations are provided by the adapters-out-persistence module.
 */
public interface CustomerRepositoryPort {
    
    Customer save(Customer customer);
    
    Optional<Customer> findById(UUID id);
    
    Optional<Customer> findByTenantIdAndPhoneNumber(UUID tenantId, String phoneNumber);
    
    Optional<Customer> findByTenantIdAndWhatsappUserId(UUID tenantId, String whatsappUserId);
}
