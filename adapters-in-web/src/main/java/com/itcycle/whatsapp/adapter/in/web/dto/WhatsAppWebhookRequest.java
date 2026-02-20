package com.itcycle.whatsapp.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * WhatsAppWebhookRequest - represents the incoming webhook payload from WhatsApp Cloud API.
 * Based on Meta Cloud API webhook structure.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhatsAppWebhookRequest {
    
    @JsonProperty("object")
    private String object;  // Should be "whatsapp_business_account"
    
    @JsonProperty("entry")
    private List<Entry> entry;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Entry {
        private String id;  // WhatsApp Business Account ID
        
        @JsonProperty("changes")
        private List<Change> changes;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Change {
        private String field;  // "messages"
        
        @JsonProperty("value")
        private Value value;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Value {
        @JsonProperty("messaging_product")
        private String messagingProduct;  // "whatsapp"
        
        @JsonProperty("metadata")
        private Metadata metadata;
        
        @JsonProperty("contacts")
        private List<Contact> contacts;
        
        @JsonProperty("messages")
        private List<MessagePayload> messages;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        @JsonProperty("display_phone_number")
        private String displayPhoneNumber;
        
        @JsonProperty("phone_number_id")
        private String phoneNumberId;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Contact {
        @JsonProperty("profile")
        private Profile profile;
        
        @JsonProperty("wa_id")
        private String waId;  // WhatsApp User ID
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Profile {
        private String name;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessagePayload {
        private String from;
        private String id;
        private String timestamp;
        private String type;  // text, image, audio, video, document, etc.
        
        @JsonProperty("text")
        private TextContent text;
        
        @JsonProperty("image")
        private MediaContent image;
        
        @JsonProperty("audio")
        private MediaContent audio;
        
        @JsonProperty("video")
        private MediaContent video;
        
        @JsonProperty("document")
        private MediaContent document;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextContent {
        private String body;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MediaContent {
        private String id;
        private String link;
        
        @JsonProperty("mime_type")
        private String mimeType;
        
        private String caption;
        private String filename;
    }
}
