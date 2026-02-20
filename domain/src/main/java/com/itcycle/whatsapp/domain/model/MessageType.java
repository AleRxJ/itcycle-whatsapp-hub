package com.itcycle.whatsapp.domain.model;

/**
 * MessageType - type of WhatsApp message content.
 * Value object representing different message payload types.
 */
public enum MessageType {
    TEXT,
    IMAGE,
    AUDIO,
    VIDEO,
    DOCUMENT,
    LOCATION,
    CONTACT,
    STICKER,
    INTERACTIVE,
    TEMPLATE,
    UNKNOWN
}
