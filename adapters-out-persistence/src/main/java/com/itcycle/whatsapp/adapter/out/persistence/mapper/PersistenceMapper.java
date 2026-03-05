package com.itcycle.whatsapp.adapter.out.persistence.mapper;

import com.itcycle.whatsapp.adapter.out.persistence.entity.*;
import com.itcycle.whatsapp.domain.model.*;
import org.springframework.stereotype.Component;

/**
 * PersistenceMapper - maps between domain models and JPA entities.
 */
@Component
public class PersistenceMapper {
    
    // Tenant mappings
    public TenantEntity toEntity(Tenant domain) {
        if (domain == null) return null;
        return TenantEntity.builder()
                .id(domain.getId())
                .companyName(domain.getCompanyName())
                .whatsappBusinessAccountId(domain.getWhatsappBusinessAccountId())
                .whatsappPhoneNumberId(domain.getWhatsappPhoneNumberId())
                .apiKey(domain.getApiKey())
                .active(domain.isActive())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
    
    public Tenant toDomain(TenantEntity entity) {
        if (entity == null) return null;
        return Tenant.builder()
                .id(entity.getId())
                .companyName(entity.getCompanyName())
                .whatsappBusinessAccountId(entity.getWhatsappBusinessAccountId())
                .whatsappPhoneNumberId(entity.getWhatsappPhoneNumberId())
                .apiKey(entity.getApiKey())
                .active(entity.isActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    // Customer mappings
    public CustomerEntity toEntity(Customer domain) {
        if (domain == null) return null;
        return CustomerEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .phoneNumber(domain.getPhoneNumber())
                .whatsappUserId(domain.getWhatsappUserId())
                .name(domain.getName())
                .email(domain.getEmail())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
    
    public Customer toDomain(CustomerEntity entity) {
        if (entity == null) return null;
        return Customer.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .phoneNumber(entity.getPhoneNumber())
                .whatsappUserId(entity.getWhatsappUserId())
                .name(entity.getName())
                .email(entity.getEmail())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    // Conversation mappings
    public ConversationEntity toEntity(Conversation domain) {
        if (domain == null) return null;
        return ConversationEntity.builder()
                .id(domain.getId())
                .tenantId(domain.getTenantId())
                .customerId(domain.getCustomerId())
                .status(toEntityStatus(domain.getStatus()))
                .startedAt(domain.getStartedAt())
                .lastMessageAt(domain.getLastMessageAt())
                .closedAt(domain.getClosedAt())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }
    
    public Conversation toDomain(ConversationEntity entity) {
        if (entity == null) return null;
        return Conversation.builder()
                .id(entity.getId())
                .tenantId(entity.getTenantId())
                .customerId(entity.getCustomerId())
                .status(toDomainStatus(entity.getStatus()))
                .startedAt(entity.getStartedAt())
                .lastMessageAt(entity.getLastMessageAt())
                .closedAt(entity.getClosedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
    
    // Message mappings
    public MessageEntity toEntity(Message domain) {
        if (domain == null) return null;
        return MessageEntity.builder()
                .id(domain.getId())
                .conversationId(domain.getConversationId())
                .tenantId(domain.getTenantId())
                .whatsappMessageId(domain.getWhatsappMessageId())
                .type(toEntityMessageType(domain.getType()))
                .direction(toEntityDirection(domain.getDirection()))
                .content(domain.getContent())
                .mediaUrl(domain.getMediaUrl())
                .mimeType(domain.getMimeType())
                .timestamp(domain.getTimestamp())
                .createdAt(domain.getCreatedAt())
                .build();
    }
    
    public Message toDomain(MessageEntity entity) {
        if (entity == null) return null;
        return Message.builder()
                .id(entity.getId())
                .conversationId(entity.getConversationId())
                .tenantId(entity.getTenantId())
                .whatsappMessageId(entity.getWhatsappMessageId())
                .type(toDomainMessageType(entity.getType()))
                .direction(toDomainDirection(entity.getDirection()))
                .content(entity.getContent())
                .mediaUrl(entity.getMediaUrl())
                .mimeType(entity.getMimeType())
                .timestamp(entity.getTimestamp())
                .createdAt(entity.getCreatedAt())
                .build();
    }
    
    // Enum mappings
    private ConversationEntity.ConversationStatusEnum toEntityStatus(ConversationStatus status) {
        if (status == null) return null;
        return ConversationEntity.ConversationStatusEnum.valueOf(status.name());
    }
    
    private ConversationStatus toDomainStatus(ConversationEntity.ConversationStatusEnum status) {
        if (status == null) return null;
        return ConversationStatus.valueOf(status.name());
    }
    
    private MessageEntity.MessageTypeEnum toEntityMessageType(MessageType type) {
        if (type == null) return null;
        return MessageEntity.MessageTypeEnum.valueOf(type.name());
    }
    
    private MessageType toDomainMessageType(MessageEntity.MessageTypeEnum type) {
        if (type == null) return null;
        return MessageType.valueOf(type.name());
    }
    
    private MessageEntity.MessageDirectionEnum toEntityDirection(MessageDirection direction) {
        if (direction == null) return null;
        return MessageEntity.MessageDirectionEnum.valueOf(direction.name());
    }
    
    private MessageDirection toDomainDirection(MessageEntity.MessageDirectionEnum direction) {
        if (direction == null) return null;
        return MessageDirection.valueOf(direction.name());
    }
}
