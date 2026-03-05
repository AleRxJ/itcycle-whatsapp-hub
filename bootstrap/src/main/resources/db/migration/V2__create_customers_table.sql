-- V2__create_customers_table.sql
-- Create customers table

CREATE TABLE customers (
    id UUID PRIMARY KEY,
    tenant_id UUID NOT NULL,
    phone_number VARCHAR(50) NOT NULL,
    whatsapp_user_id VARCHAR(255),
    name VARCHAR(255),
    email VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    CONSTRAINT fk_customers_tenant FOREIGN KEY (tenant_id) REFERENCES tenants(id) ON DELETE CASCADE,
    CONSTRAINT uk_tenant_phone UNIQUE (tenant_id, phone_number),
    CONSTRAINT uk_tenant_whatsapp_id UNIQUE (tenant_id, whatsapp_user_id)
);

CREATE INDEX idx_customers_tenant_id ON customers(tenant_id);
CREATE INDEX idx_customers_phone_number ON customers(phone_number);
CREATE INDEX idx_customers_whatsapp_user_id ON customers(whatsapp_user_id);

COMMENT ON TABLE customers IS 'End-users who interact via WhatsApp';
COMMENT ON COLUMN customers.phone_number IS 'Customer phone number in E.164 format';
COMMENT ON COLUMN customers.whatsapp_user_id IS 'WhatsApp User ID (wa_id) from Meta';
