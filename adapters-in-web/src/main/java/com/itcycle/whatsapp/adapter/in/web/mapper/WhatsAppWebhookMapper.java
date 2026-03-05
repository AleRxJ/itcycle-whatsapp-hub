package com.itcycle.whatsapp.adapter.in.web.mapper;

import com.itcycle.whatsapp.adapter.in.web.dto.WhatsAppWebhookRequest;
import com.itcycle.whatsapp.application.dto.IncomingMessageCommand;
import org.springframework.stereotype.Component;

/**
 * WhatsAppWebhookMapper - maps webhook DTOs to application commands.
 */
@Component
public class WhatsAppWebhookMapper {
    
    public IncomingMessageCommand toCommand(WhatsAppWebhookRequest request) {
        if (request == null || request.getEntry() == null || request.getEntry().isEmpty()) {
            throw new IllegalArgumentException("Invalid webhook payload");
        }
        
        WhatsAppWebhookRequest.Entry entry = request.getEntry().get(0);
        if (entry.getChanges() == null || entry.getChanges().isEmpty()) {
            throw new IllegalArgumentException("No changes in webhook payload");
        }
        
        WhatsAppWebhookRequest.Change change = entry.getChanges().get(0);
        WhatsAppWebhookRequest.Value value = change.getValue();
        
        if (value.getMessages() == null || value.getMessages().isEmpty()) {
            throw new IllegalArgumentException("No messages in webhook payload");
        }
        
        WhatsAppWebhookRequest.MessagePayload message = value.getMessages().get(0);
        WhatsAppWebhookRequest.Contact contact = value.getContacts() != null && !value.getContacts().isEmpty() 
                ? value.getContacts().get(0) 
                : null;
        
        IncomingMessageCommand.IncomingMessageCommandBuilder builder = IncomingMessageCommand.builder()
                .whatsappMessageId(message.getId())
                .whatsappPhoneNumberId(value.getMetadata().getPhoneNumberId())
                .fromPhoneNumber(message.getFrom())
                .whatsappUserId(contact != null ? contact.getWaId() : message.getFrom())
                .customerName(contact != null && contact.getProfile() != null ? contact.getProfile().getName() : null)
                .messageType(message.getType())
                .timestamp(parseLong(message.getTimestamp()));
        
        // Extract content based on message type
        switch (message.getType().toLowerCase()) {
            case "text":
                if (message.getText() != null) {
                    builder.textContent(message.getText().getBody());
                }
                break;
            case "image":
                if (message.getImage() != null) {
                    builder.mediaId(message.getImage().getId())
                           .mediaUrl(message.getImage().getLink())
                           .mimeType(message.getImage().getMimeType())
                           .mediaCaption(message.getImage().getCaption())
                           .textContent(message.getImage().getCaption());
                }
                break;
            case "audio":
                if (message.getAudio() != null) {
                    builder.mediaId(message.getAudio().getId())
                           .mediaUrl(message.getAudio().getLink())
                           .mimeType(message.getAudio().getMimeType());
                }
                break;
            case "video":
                if (message.getVideo() != null) {
                    builder.mediaId(message.getVideo().getId())
                           .mediaUrl(message.getVideo().getLink())
                           .mimeType(message.getVideo().getMimeType())
                           .mediaCaption(message.getVideo().getCaption())
                           .textContent(message.getVideo().getCaption());
                }
                break;
            case "document":
                if (message.getDocument() != null) {
                    builder.mediaId(message.getDocument().getId())
                           .mediaUrl(message.getDocument().getLink())
                           .mimeType(message.getDocument().getMimeType())
                           .filename(message.getDocument().getFilename())
                           .textContent(message.getDocument().getFilename());
                }
                break;
        }
        
        return builder.build();
    }
    
    private Long parseLong(String value) {
        if (value == null) return null;
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
