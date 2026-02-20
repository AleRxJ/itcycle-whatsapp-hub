package com.itcycle.whatsapp.adapter.out.persistence.adapter;

import com.itcycle.whatsapp.adapter.out.persistence.mapper.PersistenceMapper;
import com.itcycle.whatsapp.adapter.out.persistence.repository.MessageJpaRepository;
import com.itcycle.whatsapp.domain.model.Message;
import com.itcycle.whatsapp.domain.port.out.MessageRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * MessageRepositoryAdapter - implements MessageRepositoryPort using JPA.
 */
@Component
@RequiredArgsConstructor
public class MessageRepositoryAdapter implements MessageRepositoryPort {
    
    private final MessageJpaRepository jpaRepository;
    private final PersistenceMapper mapper;
    
    @Override
    public Message save(Message message) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(message)));
    }
    
    @Override
    public Optional<Message> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }
    
    @Override
    public List<Message> findByConversationId(UUID conversationId) {
        return jpaRepository.findByConversationIdOrderByTimestampAsc(conversationId)
                .stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<Message> findByWhatsappMessageId(String whatsappMessageId) {
        return jpaRepository.findByWhatsappMessageId(whatsappMessageId).map(mapper::toDomain);
    }
}
