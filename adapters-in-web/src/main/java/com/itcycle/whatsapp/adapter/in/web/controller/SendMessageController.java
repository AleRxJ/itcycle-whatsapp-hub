package com.itcycle.whatsapp.adapter.in.web.controller;

import com.itcycle.whatsapp.domain.port.out.WhatsAppClientPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;

/**
 * SendMessageController - REST controller for sending WhatsApp messages.
 * This endpoint is called by n8n workflows to send responses back to customers.
 */
@RestController
@RequestMapping("/api/whatsapp/send")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "WhatsApp Send", description = "Endpoints for sending WhatsApp messages")
public class SendMessageController {
    
    private final WhatsAppClientPort whatsAppClient;
    
    /**
     * Send a text message to a WhatsApp number
     */
    @PostMapping
    @Operation(summary = "Send WhatsApp message", description = "Send a text message through WhatsApp Cloud API")
    public ResponseEntity<SendMessageResponse> sendMessage(@Valid @RequestBody SendMessageRequest request) {
        
        log.info("Sending WhatsApp message to: {} from tenant: {}", request.getTo(), request.getTenantId());
        
        try {
            // Send message through WhatsApp client
            String messageId = whatsAppClient.sendMessage(
                request.getTenantId(),
                request.getTo(),
                request.getMessage()
            );
            
            SendMessageResponse response = SendMessageResponse.builder()
                    .status("sent")
                    .messageId(messageId)
                    .to(request.getTo())
                    .message("Message sent successfully")
                    .build();
            
            log.info("Message sent successfully. MessageId: {}", messageId);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error sending WhatsApp message to {}: {}", request.getTo(), e.getMessage(), e);
            
            SendMessageResponse response = SendMessageResponse.builder()
                    .status("error")
                    .messageId(null)
                    .to(request.getTo())
                    .message("Error sending message: " + e.getMessage())
                    .build();
            
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
    /**
     * Send message request DTO
     */
    @Data
    public static class SendMessageRequest {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;
        
        @NotBlank(message = "Recipient phone number is required")
        private String to;
        
        @NotBlank(message = "Message text is required")
        private String message;
    }
    
    /**
     * Send message response DTO
     */
    @Data
    @lombok.Builder
    public static class SendMessageResponse {
        private String status;      // "sent" or "error"
        private String messageId;   // WhatsApp message ID
        private String to;          // Recipient phone number
        private String message;     // Status message
    }
}