package com.itcycle.whatsapp.adapter.out.external.whatsapp;

import com.itcycle.whatsapp.domain.port.out.WhatsAppClientPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * MockWhatsAppClient - Mock implementation for testing without Meta API.
 * Remove @Primary annotation when Meta credentials are configured.
 */
@Component
@Primary  // This makes it the default implementation (higher priority than MetaWhatsAppClient)
@Slf4j
public class MockWhatsAppClient implements WhatsAppClientPort {
    
    @Override
    public String sendMessage(String tenantId, String to, String text) {
        log.info("🔷 [MOCK] Sending text message to: {}", to);
        log.info("🔷 [MOCK] Tenant: {}", tenantId);
        log.info("🔷 [MOCK] Message: {}", text.substring(0, Math.min(100, text.length())));
        
        // Simulate successful message sending
        String mockMessageId = "wamid.mock_" + UUID.randomUUID().toString().substring(0, 8);
        
        log.info("✅ [MOCK] Message sent successfully! Mock ID: {}", mockMessageId);
        
        return mockMessageId;
    }
    
    @Override
    public String sendMediaMessage(String tenantId, String to, String mediaUrl, String caption, String mediaType) {
        log.info("🔷 [MOCK] Sending {} media message to: {}", mediaType, to);
        log.info("🔷 [MOCK] Media URL: {}", mediaUrl);
        log.info("🔷 [MOCK] Caption: {}", caption);
        
        String mockMessageId = "wamid.mock_media_" + UUID.randomUUID().toString().substring(0, 8);
        
        log.info("✅ [MOCK] Media message sent successfully! Mock ID: {}", mockMessageId);
        
        return mockMessageId;
    }
    
    @Override
    public void markAsRead(String tenantId, String messageId) {
        log.info("🔷 [MOCK] Marking message as read: {}", messageId);
        log.info("✅ [MOCK] Message marked as read!");
    }
}
