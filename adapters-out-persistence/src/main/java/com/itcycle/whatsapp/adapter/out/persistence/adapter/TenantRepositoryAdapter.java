package com.itcycle.whatsapp.adapter.out.persistence.adapter;

import com.itcycle.whatsapp.adapter.out.persistence.mapper.PersistenceMapper;
import com.itcycle.whatsapp.adapter.out.persistence.repository.TenantJpaRepository;
import com.itcycle.whatsapp.domain.model.Tenant;
import com.itcycle.whatsapp.domain.port.out.TenantRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * TenantRepositoryAdapter - implements TenantRepositoryPort using JPA.
 */
@Component
@RequiredArgsConstructor
public class TenantRepositoryAdapter implements TenantRepositoryPort {
    
    private final TenantJpaRepository jpaRepository;
    private final PersistenceMapper mapper;
    
    @Override
    public Tenant save(Tenant tenant) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(tenant)));
    }
    
    @Override
    public Optional<Tenant> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    
    @Override
    public Optional<Tenant> findByApiKey(String apiKey) {
        return jpaRepository.findByApiKey(apiKey).map(mapper::toDomain);
    }
    
    @Override
    public Optional<Tenant> findByWhatsappPhoneNumberId(String whatsappPhoneNumberId) {
        return jpaRepository.findByWhatsappPhoneNumberId(whatsappPhoneNumberId).map(mapper::toDomain);
    }
}
