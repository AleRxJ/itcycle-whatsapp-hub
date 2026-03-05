package com.itcycle.whatsapp.adapter.out.persistence.repository;

import com.itcycle.whatsapp.adapter.out.persistence.entity.ConversationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * ConversationJpaRepository - Spring Data JPA repository for ConversationEntity.
 */
@Repository
public interface ConversationJpaRepository extends JpaRepository<ConversationEntity, UUID> {
    
    @Query("SELECT c FROM ConversationEntity c WHERE c.tenantId = :tenantId " +
           "AND c.customerId = :customerId AND c.status = 'ACTIVE'")
    Optional<ConversationEntity> findActiveByTenantAndCustomer(UUID tenantId, UUID customerId);
    
    @Query("SELECT c FROM ConversationEntity c WHERE c.tenantId = :tenantId " +
           "AND c.customerId = :customerId ORDER BY c.lastMessageAt DESC")
    Optional<ConversationEntity> findLatestByTenantAndCustomer(UUID tenantId, UUID customerId);
}
