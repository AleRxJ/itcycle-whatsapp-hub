package com.itcycle.whatsapp.domain.port.out;

import java.util.Map;

/**
 * WhatsAppClientPort - output port for WhatsApp provider integration.
 * This interface defines the contract for sending messages to WhatsApp.
 * Implementations can use Meta Cloud API, Twilio, or other providers.
 */
public interface WhatsAppClientPort {
    
    /**
     * Send a text message via WhatsApp
     * @param tenantId tenant identifier for configuration
     * @param to recipient phone number (E.164 format)
     * @param text message content
     * @return WhatsApp message ID
     */
    String sendMessage(String tenantId, String to, String text);
    
    /**
     * Send a media message via WhatsApp
     * @param tenantId tenant identifier
     * @param to recipient phone number
     * @param mediaUrl URL of the media file
     * @param caption optional caption
     * @param mediaType type: image, audio, video, document
     * @return WhatsApp message ID
     */
    String sendMediaMessage(String tenantId, String to, String mediaUrl, String caption, String mediaType);
    
    /**
     * Mark a message as read
     * @param tenantId tenant identifier
     * @param messageId WhatsApp message ID
     */
    void markAsRead(String tenantId, String messageId);
}
