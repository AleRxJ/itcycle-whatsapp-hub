package com.itcycle.whatsapp.adapter.out.persistence.adapter;

import com.itcycle.whatsapp.adapter.out.persistence.mapper.PersistenceMapper;
import com.itcycle.whatsapp.adapter.out.persistence.repository.ConversationJpaRepository;
import com.itcycle.whatsapp.domain.model.Conversation;
import com.itcycle.whatsapp.domain.port.out.ConversationRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * ConversationRepositoryAdapter - implements ConversationRepositoryPort using JPA.
 */
@Component
@RequiredArgsConstructor
public class ConversationRepositoryAdapter implements ConversationRepositoryPort {
    
    private final ConversationJpaRepository jpaRepository;
    private final PersistenceMapper mapper;
    
    @Override
    public Conversation save(Conversation conversation) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(conversation)));
    }
    
    @Override
    public Optional<Conversation> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    
    @Override
    public Optional<Conversation> findActiveByTenantAndCustomer(UUID tenantId, UUID customerId) {
        return jpaRepository.findActiveByTenantAndCustomer(tenantId, customerId).map(mapper::toDomain);
    }
    
    @Override
    public Optional<Conversation> findLatestByTenantAndCustomer(UUID tenantId, UUID customerId) {
        return jpaRepository.findLatestByTenantAndCustomer(tenantId, customerId).map(mapper::toDomain);
    }
}
