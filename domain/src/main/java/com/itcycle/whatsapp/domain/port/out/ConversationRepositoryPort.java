package com.itcycle.whatsapp.domain.port.out;

import com.itcycle.whatsapp.domain.model.Conversation;
import java.util.Optional;
import java.util.UUID;

/**
 * ConversationRepositoryPort - output port for conversation persistence operations.
 * This interface defines the contract for storing and retrieving conversations.
 * Implementations are provided by the adapters-out-persistence module.
 */
public interface ConversationRepositoryPort {
    
    Conversation save(Conversation conversation);
    
    Optional<Conversation> findById(UUID id);
    
    Optional<Conversation> findActiveByTenantAndCustomer(UUID tenantId, UUID customerId);
    
    Optional<Conversation> findLatestByTenantAndCustomer(UUID tenantId, UUID customerId);
}
