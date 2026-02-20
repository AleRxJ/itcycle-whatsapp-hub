package com.itcycle.whatsapp.domain.port.out;

import com.itcycle.whatsapp.domain.model.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * MessageRepositoryPort - output port for message persistence operations.
 * This interface defines the contract for storing and retrieving messages.
 * Implementations are provided by the adapters-out-persistence module.
 */
public interface MessageRepositoryPort {
    
    Message save(Message message);
    
    Optional<Message> findById(UUID id);
    
    List<Message> findByConversationId(UUID conversationId);
    
    Optional<Message> findByWhatsappMessageId(String whatsappMessageId);
}
