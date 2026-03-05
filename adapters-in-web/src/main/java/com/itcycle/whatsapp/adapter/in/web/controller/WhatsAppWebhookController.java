package com.itcycle.whatsapp.adapter.in.web.controller;

import com.itcycle.whatsapp.adapter.in.web.dto.MessageResponseDto;
import com.itcycle.whatsapp.adapter.in.web.dto.WhatsAppWebhookRequest;
import com.itcycle.whatsapp.adapter.in.web.mapper.WhatsAppWebhookMapper;
import com.itcycle.whatsapp.application.dto.IncomingMessageCommand;
import com.itcycle.whatsapp.application.dto.MessageResponse;
import com.itcycle.whatsapp.application.port.in.HandleIncomingMessageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * WhatsAppWebhookController - REST controller for receiving WhatsApp webhook events.
 * This is the entry point for incoming messages from WhatsApp Cloud API.
 */
@RestController
@RequestMapping("/webhooks/whatsapp")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "WhatsApp Webhooks", description = "Endpoints for WhatsApp Cloud API webhooks")
public class WhatsAppWebhookController {
    
    private final HandleIncomingMessageUseCase handleIncomingMessageUseCase;
    private final WhatsAppWebhookMapper webhookMapper;
    
    /**
     * Webhook verification endpoint (GET) - used by Meta to verify the webhook URL
     */
    @GetMapping
    @Operation(summary = "Verify webhook", description = "Endpoint for Meta webhook verification")
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String verifyToken) {
        
        log.info("Webhook verification requested: mode={}, token={}", mode, verifyToken);
        
        // TODO: Validate verify token against configured value
        String expectedToken = "itcycle_whatsapp_hub_verify_token";  // Should come from config
        
        if ("subscribe".equals(mode) && expectedToken.equals(verifyToken)) {
            log.info("Webhook verified successfully");
            return ResponseEntity.ok(challenge);
        }
        
        log.warn("Webhook verification failed");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Verification failed");
    }
    
    /**
     * Webhook endpoint for incoming WhatsApp messages
     */
    @PostMapping
    @Operation(summary = "Receive WhatsApp messages", description = "Webhook endpoint for incoming WhatsApp messages")
    public ResponseEntity<MessageResponseDto> receiveMessage(@RequestBody WhatsAppWebhookRequest request) {
        
        log.info("Received WhatsApp webhook: {}", request);
        
        try {
            // Extract the first message from webhook payload
            IncomingMessageCommand command = webhookMapper.toCommand(request);
            
            // Process message through use case
            MessageResponse response = handleIncomingMessageUseCase.handle(command);
            
            // Map to API response
            MessageResponseDto responseDto = MessageResponseDto.builder()
                    .messageId(response.getMessageId())
                    .conversationId(response.getConversationId())
                    .customerId(response.getCustomerId())
                    .status(response.getStatus())
                    .message("Message received successfully")
                    .build();
            
            return ResponseEntity.ok(responseDto);
            
        } catch (Exception e) {
            log.error("Error processing webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponseDto.builder()
                            .status("error")
                            .message(e.getMessage())
                            .build());
        }
    }
}
