-- V5__insert_sample_tenant.sql
-- Insert a sample tenant for testing

INSERT INTO tenants (
    id, 
    company_name, 
    whatsapp_business_account_id,
    whatsapp_phone_number_id, 
    api_key, 
    active, 
    created_at
) VALUES (
    'a1b2c3d4-e5f6-4a5b-9c8d-1e2f3a4b5c6d'::uuid,
    'iTCycle Demo Company',
    '123456789012345',
    'phone_number_id_demo_123',
    'demo_api_key_itcycle_2024_change_in_production',
    true,
    CURRENT_TIMESTAMP
);

COMMENT ON TABLE tenants IS 'Sample tenant created for development and testing. Change credentials in production.';
