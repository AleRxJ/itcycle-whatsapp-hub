package com.itcycle.whatsapp.adapter.in.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * MessageResponseDto - API response after processing a message.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {
    private UUID messageId;
    private UUID conversationId;
    private UUID customerId;
    private String status;
    private String message;
}
