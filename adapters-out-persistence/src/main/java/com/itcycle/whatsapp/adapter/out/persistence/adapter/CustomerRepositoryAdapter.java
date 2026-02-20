package com.itcycle.whatsapp.adapter.out.persistence.adapter;

import com.itcycle.whatsapp.adapter.out.persistence.mapper.PersistenceMapper;
import com.itcycle.whatsapp.adapter.out.persistence.repository.CustomerJpaRepository;
import com.itcycle.whatsapp.domain.model.Customer;
import com.itcycle.whatsapp.domain.port.out.CustomerRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * CustomerRepositoryAdapter - implements CustomerRepositoryPort using JPA.
 */
@Component
@RequiredArgsConstructor
public class CustomerRepositoryAdapter implements CustomerRepositoryPort {
    
    private final CustomerJpaRepository jpaRepository;
    private final PersistenceMapper mapper;
    
    @Override
    public Customer save(Customer customer) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(customer)));
    }
    
    @Override
    public Optional<Customer> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    
    @Override
    public Optional<Customer> findByTenantIdAndPhoneNumber(UUID tenantId, String phoneNumber) {
        return jpaRepository.findByTenantIdAndPhoneNumber(tenantId, phoneNumber).map(mapper::toDomain);
    }
    
    @Override
    public Optional<Customer> findByTenantIdAndWhatsappUserId(UUID tenantId, String whatsappUserId) {
        return jpaRepository.findByTenantIdAndWhatsappUserId(tenantId, whatsappUserId).map(mapper::toDomain);
    }
}
