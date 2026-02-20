package com.itcycle.whatsapp.application.port.in;

import com.itcycle.whatsapp.application.dto.IncomingMessageCommand;
import com.itcycle.whatsapp.application.dto.MessageResponse;

/**
 * HandleIncomingMessageUseCase - input port for handling incoming WhatsApp messages.
 * This is the main use case interface that orchestrates the business logic.
 */
public interface HandleIncomingMessageUseCase {
    
    /**
     * Process an incoming WhatsApp message
     * @param command contains the message data from webhook
     * @return MessageResponse with created entities
     */
    MessageResponse handle(IncomingMessageCommand command);
}
