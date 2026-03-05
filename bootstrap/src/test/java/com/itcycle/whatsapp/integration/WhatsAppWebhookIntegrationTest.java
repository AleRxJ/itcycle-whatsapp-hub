package com.itcycle.whatsapp.integration;

import com.itcycle.whatsapp.ItcycleWhatsappHubApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration test for WhatsApp webhook endpoint using Testcontainers.
 * Tests the complete flow from HTTP request to database persistence.
 */
@SpringBootTest(classes = ItcycleWhatsappHubApplication.class)
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
class WhatsAppWebhookIntegrationTest {
    
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("whatsapp_hub_test")
            .withUsername("test")
            .withPassword("test");
    
    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitmq = new RabbitMQContainer("rabbitmq:3-management-alpine");
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void shouldVerifyWebhook() throws Exception {
        mockMvc.perform(get("/webhooks/whatsapp")
                        .param("hub.mode", "subscribe")
                        .param("hub.challenge", "test_challenge_123")
                        .param("hub.verify_token", "itcycle_whatsapp_hub_verify_token"))
                .andExpect(status().isOk())
                .andExpect(content().string("test_challenge_123"));
    }
    
    @Test
    void shouldRejectWebhookWithInvalidToken() throws Exception {
        mockMvc.perform(get("/webhooks/whatsapp")
                        .param("hub.mode", "subscribe")
                        .param("hub.challenge", "test_challenge_123")
                        .param("hub.verify_token", "invalid_token"))
                .andExpect(status().isForbidden());
    }
    
    @Test
    void shouldReceiveIncomingTextMessage() throws Exception {
        String webhookPayload = """
                {
                  "object": "whatsapp_business_account",
                  "entry": [{
                    "id": "123456789012345",
                    "changes": [{
                      "value": {
                        "messaging_product": "whatsapp",
                        "metadata": {
                          "display_phone_number": "+1234567890",
                          "phone_number_id": "phone_number_id_demo_123"
                        },
                        "contacts": [{
                          "profile": {
                            "name": "John Doe"
                          },
                          "wa_id": "1234567890"
                        }],
                        "messages": [{
                          "from": "1234567890",
                          "id": "wamid.test123",
                          "timestamp": "1640000000",
                          "type": "text",
                          "text": {
                            "body": "Hello, this is a test message!"
                          }
                        }]
                      },
                      "field": "messages"
                    }]
                  }]
                }
                """;
        
        mockMvc.perform(post("/webhooks/whatsapp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(webhookPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("processed"))
                .andExpect(jsonPath("$.messageId").exists())
                .andExpect(jsonPath("$.conversationId").exists())
                .andExpect(jsonPath("$.customerId").exists());
    }
    
    @Test
    void shouldHandleImageMessage() throws Exception {
        String webhookPayload = """
                {
                  "object": "whatsapp_business_account",
                  "entry": [{
                    "id": "123456789012345",
                    "changes": [{
                      "value": {
                        "messaging_product": "whatsapp",
                        "metadata": {
                          "display_phone_number": "+1234567890",
                          "phone_number_id": "phone_number_id_demo_123"
                        },
                        "contacts": [{
                          "profile": {
                            "name": "Jane Doe"
                          },
                          "wa_id": "9876543210"
                        }],
                        "messages": [{
                          "from": "9876543210",
                          "id": "wamid.test456",
                          "timestamp": "1640000100",
                          "type": "image",
                          "image": {
                            "id": "img123",
                            "link": "https://example.com/image.jpg",
                            "mime_type": "image/jpeg",
                            "caption": "Check out this image!"
                          }
                        }]
                      },
                      "field": "messages"
                    }]
                  }]
                }
                """;
        
        mockMvc.perform(post("/webhooks/whatsapp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(webhookPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("processed"))
                .andExpect(jsonPath("$.messageId").exists());
    }
}
