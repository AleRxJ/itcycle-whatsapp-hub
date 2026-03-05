package com.itcycle.whatsapp.adapter.out.persistence.repository;

import com.itcycle.whatsapp.adapter.out.persistence.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MessageJpaRepository - Spring Data JPA repository for MessageEntity.
 */
@Repository
public interface MessageJpaRepository extends JpaRepository<MessageEntity, UUID> {
    
    List<MessageEntity> findByConversationIdOrderByTimestampAsc(UUID conversationId);
    
    Optional<MessageEntity> findByWhatsappMessageId(String whatsappMessageId);
}
