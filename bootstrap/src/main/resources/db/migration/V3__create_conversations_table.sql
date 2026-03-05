-- V3__create_conversations_table.sql
-- Create conversations table

CREATE TABLE conversations (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'CLOSED', 'ARCHIVED')),
    started_at TIMESTAMP NOT NULL,
    last_message_at TIMESTAMP,
    closed_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_conversations_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id) ON DELETE CASCADE,
    CONSTRAINT fk_conversations_customer FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

CREATE INDEX idx_conversations_tenant_customer ON conversations(tenant_id, customer_id);
CREATE INDEX idx_conversations_status ON conversations(status);
CREATE INDEX idx_conversations_last_message_at ON conversations(last_message_at DESC);

COMMENT ON TABLE conversations IS 'Conversation threads between customers and business';
COMMENT ON COLUMN conversations.status IS 'Current status: ACTIVE, CLOSED, or ARCHIVED';
COMMENT ON COLUMN conversations.last_message_at IS 'Timestamp of the most recent message';
