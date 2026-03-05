package com.itcycle.whatsapp.adapter.out.persistence.repository;

import com.itcycle.whatsapp.adapter.out.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * CustomerJpaRepository - Spring Data JPA repository for CustomerEntity.
 */
@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
    
    Optional<CustomerEntity> findByTenantIdAndPhoneNumber(UUID tenantId, String phoneNumber);
    
    Optional<CustomerEntity> findByTenantIdAndWhatsappUserId(UUID tenantId, String whatsappUserId);
}
