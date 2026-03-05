package com.itcycle.whatsapp.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * IncomingMessageCommand - command object for processing incoming WhatsApp messages.
 * Contains all the information extracted from the webhook payload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncomingMessageCommand {
    private String whatsappMessageId;
    private String whatsappPhoneNumberId;  // Business phone number
    private String fromPhoneNumber;        // Customer phone number
    private String whatsappUserId;         // Customer WhatsApp ID
    private String customerName;           // Customer name from WhatsApp profile
    private String messageType;            // text, image, audio, video, document
    private String textContent;
    private String mediaUrl;
    private String mimeType;
    private Long timestamp;                // Unix timestamp from WhatsApp
    
    // Enhanced media support
    private String mediaId;                // WhatsApp media ID (for downloading)
    private String mediaCaption;           // Caption for media files
    private String filename;               // Original filename (for documents)
    private Long fileSize;                 // File size in bytes
    private String sha256;                 // SHA256 hash of media file
}
