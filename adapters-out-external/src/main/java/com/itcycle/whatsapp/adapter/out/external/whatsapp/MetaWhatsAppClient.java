package com.itcycle.whatsapp.adapter.out.external.whatsapp;

import com.itcycle.whatsapp.domain.port.out.WhatsAppClientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * MetaWhatsAppClient - WhatsApp Cloud API client implementation.
 * Sends messages via Meta's Cloud API.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MetaWhatsAppClient implements WhatsAppClientPort {
    
    private final RestTemplate restTemplate;
    
    @Value("${whatsapp.meta.api.url:https://graph.facebook.com/v18.0}")
    private String apiUrl;
    
    @Value("${whatsapp.meta.access.token:}")
    private String accessToken;
    
    @Override
    public String sendMessage(String tenantId, String to, String text) {
        log.info("Sending text message to {} via WhatsApp Cloud API", to);
        
        // TODO: Get phone_number_id from tenant configuration
        String phoneNumberId = "PHONE_NUMBER_ID";  // Should be loaded from tenant
        
        String url = String.format("%s/%s/messages", apiUrl, phoneNumberId);
        
        Map<String, Object> payload = Map.of(
                "messaging_product", "whatsapp",
                "recipient_type", "individual",
                "to", to,
                "type", "text",
                "text", Map.of("body", text)
        );
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                Map<String, Object> messages = (Map<String, Object>) body.get("messages");
                if (messages != null) {
                    return (String) ((Map) ((java.util.List) messages.get(0)).get(0)).get("id");
                }
            }
            
            log.warn("Unexpected response sending WhatsApp message: {}", response);
            return "unknown";
            
        } catch (Exception e) {
            log.error("Error sending WhatsApp message", e);
            throw new RuntimeException("Failed to send WhatsApp message", e);
        }
    }
    
    @Override
    public String sendMediaMessage(String tenantId, String to, String mediaUrl, String caption, String mediaType) {
        log.info("Sending {} media message to {} via WhatsApp Cloud API", mediaType, to);
        
        // TODO: Implement media message sending
        throw new UnsupportedOperationException("Media messages not yet implemented");
    }
    
    @Override
    public void markAsRead(String tenantId, String messageId) {
        log.info("Marking message {} as read", messageId);
        
        // TODO: Implement mark as read
        // Not critical for MVP
    }
}
