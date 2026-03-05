package com.itcycle.whatsapp.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

/**
 * MessageResponse - response after processing an incoming message.
 * Contains the created message ID and conversation ID.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private UUID messageId;
    private UUID conversationId;
    private UUID customerId;
    private String status;
    private String acknowledgment;
}
