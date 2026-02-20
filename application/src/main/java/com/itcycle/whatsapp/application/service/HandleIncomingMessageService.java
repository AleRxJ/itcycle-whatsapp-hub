package com.itcycle.whatsapp.application.service;

import com.itcycle.whatsapp.application.dto.IncomingMessageCommand;
import com.itcycle.whatsapp.application.dto.MessageResponse;
import com.itcycle.whatsapp.application.port.in.HandleIncomingMessageUseCase;
import com.itcycle.whatsapp.domain.event.IncomingMessageEvent;
import com.itcycle.whatsapp.domain.model.*;
import com.itcycle.whatsapp.domain.port.out.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

/**
 * HandleIncomingMessageService - implementation of the main use case.
 * Orchestrates the flow: identify tenant, find/create customer, find/create conversation,
 * save message, publish event, and optionally send acknowledgment.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class HandleIncomingMessageService implements HandleIncomingMessageUseCase {
    
    private final TenantRepositoryPort tenantRepository;
    private final CustomerRepositoryPort customerRepository;
    private final ConversationRepositoryPort conversationRepository;
    private final MessageRepositoryPort messageRepository;
    private final EventPublisherPort eventPublisher;
    private final WhatsAppClientPort whatsAppClient;
    
    @Override
    public MessageResponse handle(IncomingMessageCommand command) {
        log.info("Processing incoming message: {}", command.getWhatsappMessageId());
        
        // 1. Identify tenant by WhatsApp phone number ID
        Tenant tenant = tenantRepository.findByWhatsappPhoneNumberId(command.getWhatsappPhoneNumberId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tenant not found for phone number ID: " + command.getWhatsappPhoneNumberId()));
        
        // 2. Find or create customer
        Customer customer = findOrCreateCustomer(tenant, command);
        
        // 3. Find or create active conversation
        Conversation conversation = findOrCreateConversation(tenant, customer);
        
        // 4. Save incoming message
        Message message = saveMessage(conversation, command);
        
        // 5. Publish domain event
        publishEvent(message, conversation, customer, tenant);
        
        // 6. Send acknowledgment (optional - simple echo for MVP)
        sendAcknowledgment(tenant, customer, message);
        
        log.info("Message processed successfully: messageId={}, conversationId={}", 
                message.getId(), conversation.getId());
        
        return MessageResponse.builder()
                .messageId(message.getId())
                .conversationId(conversation.getId())
                .customerId(customer.getId())
                .status("processed")
                .acknowledgment("Message received")
                .build();
    }
    
    private Customer findOrCreateCustomer(Tenant tenant, IncomingMessageCommand command) {
        Optional<Customer> existingCustomer = customerRepository
                .findByTenantIdAndPhoneNumber(tenant.getId(), command.getFromPhoneNumber());
        
        if (existingCustomer.isPresent()) {
            return existingCustomer.get();
        }
        
        Customer newCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .tenantId(tenant.getId())
                .phoneNumber(command.getFromPhoneNumber())
                .whatsappUserId(command.getWhatsappUserId())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        
        return customerRepository.save(newCustomer);
    }
    
    private Conversation findOrCreateConversation(Tenant tenant, Customer customer) {
        Optional<Conversation> activeConversation = conversationRepository
                .findActiveByTenantAndCustomer(tenant.getId(), customer.getId());
        
        if (activeConversation.isPresent()) {
            Conversation conversation = activeConversation.get();
            conversation.setLastMessageAt(Instant.now());
            conversation.setUpdatedAt(Instant.now());
            return conversationRepository.save(conversation);
        }
        
        Conversation newConversation = Conversation.builder()
                .id(UUID.randomUUID())
                .tenantId(tenant.getId())
                .customerId(customer.getId())
                .status(ConversationStatus.ACTIVE)
                .startedAt(Instant.now())
                .lastMessageAt(Instant.now())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        
        return conversationRepository.save(newConversation);
    }
    
    private Message saveMessage(Conversation conversation, IncomingMessageCommand command) {
        Message message = Message.builder()
                .id(UUID.randomUUID())
                .conversationId(conversation.getId())
                .tenantId(conversation.getTenantId())
                .whatsappMessageId(command.getWhatsappMessageId())
                .type(parseMessageType(command.getMessageType()))
                .direction(MessageDirection.INCOMING)
                .content(command.getTextContent())
                .mediaUrl(command.getMediaUrl())
                .mimeType(command.getMimeType())
                .timestamp(command.getTimestamp() != null 
                        ? Instant.ofEpochSecond(command.getTimestamp()) 
                        : Instant.now())
                .createdAt(Instant.now())
                .build();
        
        return messageRepository.save(message);
    }
    
    private void publishEvent(Message message, Conversation conversation, Customer customer, Tenant tenant) {
        IncomingMessageEvent event = IncomingMessageEvent.builder()
                .messageId(message.getId())
                .conversationId(conversation.getId())
                .customerId(customer.getId())
                .tenantId(tenant.getId())
                .content(message.getContent())
                .messageType(message.getType().name())
                .timestamp(message.getTimestamp())
                .build();
        
        eventPublisher.publishIncomingMessage(event);
    }
    
    private void sendAcknowledgment(Tenant tenant, Customer customer, Message message) {
        try {
            // Simple echo acknowledgment for MVP
            String acknowledgment = "Message received: " + 
                    (message.getContent() != null ? message.getContent() : "[" + message.getType() + "]");
            
            whatsAppClient.sendMessage(
                    tenant.getId().toString(), 
                    customer.getPhoneNumber(), 
                    acknowledgment
            );
        } catch (Exception e) {
            log.error("Failed to send acknowledgment", e);
            // Don't fail the entire operation if acknowledgment fails
        }
    }
    
    private MessageType parseMessageType(String type) {
        if (type == null) return MessageType.UNKNOWN;
        try {
            return MessageType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            return MessageType.UNKNOWN;
        }
    }
}
