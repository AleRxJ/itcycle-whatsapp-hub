-- V1__create_tenants_table.sql
-- Create tenants table

CREATE TABLE tenants (
    id UUID PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    whatsapp_business_account_id VARCHAR(255),
    whatsapp_phone_number_id VARCHAR(255) UNIQUE,
    api_key VARCHAR(500) UNIQUE NOT NULL,
    active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE INDEX idx_tenants_api_key ON tenants(api_key);
CREATE INDEX idx_tenants_phone_number_id ON tenants(whatsapp_phone_number_id);

COMMENT ON TABLE tenants IS 'Companies/organizations using the WhatsApp Hub platform';
COMMENT ON COLUMN tenants.whatsapp_phone_number_id IS 'WhatsApp Business Phone Number ID from Meta Cloud API';
COMMENT ON COLUMN tenants.api_key IS 'API key for tenant authentication';
