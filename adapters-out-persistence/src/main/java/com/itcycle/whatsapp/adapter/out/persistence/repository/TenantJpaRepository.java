package com.itcycle.whatsapp.adapter.out.persistence.repository;

import com.itcycle.whatsapp.adapter.out.persistence.entity.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * TenantJpaRepository - Spring Data JPA repository for TenantEntity.
 */
@Repository
public interface TenantJpaRepository extends JpaRepository<TenantEntity, UUID> {
    
    Optional<TenantEntity> findByApiKey(String apiKey);
    
    Optional<TenantEntity> findByWhatsappPhoneNumberId(String whatsappPhoneNumberId);
}
