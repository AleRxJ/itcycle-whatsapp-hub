-- V4__create_messages_table.sql
-- Create messages table

CREATE TABLE messages (
    id UUID PRIMARY KEY,
    conversation_id UUID NOT NULL,
    tenant_id UUID NOT NULL,
    whatsapp_message_id VARCHAR(255) UNIQUE,
    type VARCHAR(20) NOT NULL CHECK (type IN ('TEXT', 'IMAGE', 'AUDIO', 'VIDEO', 'DOCUMENT', 'LOCATION', 'CONTACT', 'STICKER', 'INTERACTIVE', 'TEMPLATE', 'UNKNOWN')),
    direction VARCHAR(10) NOT NULL CHECK (direction IN ('INCOMING', 'OUTGOING')),
    content TEXT,
    media_url VARCHAR(1000),
    mime_type VARCHAR(100),
    timestamp TIMESTAMP NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_messages_conversation FOREIGN KEY (conversation_id) REFERENCES conversations(id) ON DELETE CASCADE,
    CONSTRAINT fk_messages_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id) ON DELETE CASCADE
);

CREATE INDEX idx_messages_conversation ON messages(conversation_id);
CREATE INDEX idx_messages_whatsapp_message_id ON messages(whatsapp_message_id);
CREATE INDEX idx_messages_timestamp ON messages(timestamp DESC);
CREATE INDEX idx_messages_tenant ON messages(tenant_id);
CREATE INDEX idx_messages_type ON messages(type);

COMMENT ON TABLE messages IS 'Individual messages within conversations';
COMMENT ON COLUMN messages.whatsapp_message_id IS 'Unique message ID from WhatsApp Cloud API';
COMMENT ON COLUMN messages.type IS 'Message content type';
COMMENT ON COLUMN messages.direction IS 'INCOMING from customer, OUTGOING to customer';
COMMENT ON COLUMN messages.media_url IS 'URL to media file for non-text messages';
