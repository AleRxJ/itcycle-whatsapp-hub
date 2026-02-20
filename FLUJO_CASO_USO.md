# Diagrama de Flujo - Caso de Uso Principal

## 📬 HandleIncomingMessage Flow

```
┌─────────────────────────────────────────────────────────────────────┐
│                         INCOMING WHATSAPP MESSAGE                    │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│  1. WhatsAppWebhookController (adapters-in-web)                     │
│     POST /webhooks/whatsapp                                         │
│     - Recibe webhook de Meta Cloud API                              │
│     - Valida estructura del payload                                 │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│  2. WhatsAppWebhookMapper                                           │
│     - Extrae datos del payload complejo                             │
│     - Convierte a IncomingMessageCommand (DTO simple)               │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│  3. HandleIncomingMessageService (application)                      │
│     Orquesta todo el flujo de negocio                               │
└─────────────────────────────────────────────────────────────────────┘
                                    │
        ┌───────────────────────────┼───────────────────────────┐
        │                           │                           │
        ▼                           ▼                           ▼
┌──────────────────┐    ┌──────────────────┐    ┌──────────────────┐
│ 3.1 Identify     │    │ 3.2 Find/Create  │    │ 3.3 Find/Create  │
│     Tenant       │    │     Customer     │    │     Conversation │
│                  │    │                  │    │                  │
│ BY: phone_number_│    │ BY: tenant_id +  │    │ BY: tenant_id +  │
│     id           │    │     phone_number │    │     customer_id  │
└──────────────────┘    └──────────────────┘    └──────────────────┘
        │                           │                           │
        └───────────────────────────┴───────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│  3.4 Save Message                                                   │
│      - Create Message entity                                        │
│      - Set conversation_id, tenant_id                               │
│      - Store in PostgreSQL                                          │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│  3.5 Publish Event                                                  │
│      - Create IncomingMessageEvent (domain event)                   │
│      - Publish to RabbitMQ (exchange: whatsapp.events)              │
│      - Trigger n8n webhook (async)                                  │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│  3.6 Send Acknowledgment (MVP: echo message)                        │
│      - Call WhatsAppClientPort.sendMessage()                        │
│      - MetaWhatsAppClient makes API call to Meta                    │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│  4. Return Response                                                 │
│     MessageResponseDto with:                                        │
│     - messageId                                                     │
│     - conversationId                                                │
│     - customerId                                                    │
│     - status: "processed"                                           │
└─────────────────────────────────────────────────────────────────────┘
```

## 🔄 Event Flow

```
┌────────────────┐         ┌──────────────┐         ┌────────────┐
│  Application   │ publishes│  RabbitMQ    │ routes  │  Consumer  │
│  (Publisher)   │────────>│  Exchange    │────────>│  (n8n)     │
│                │         │  Events      │         │            │
└────────────────┘         └──────────────┘         └────────────┘
                                   │
                                   │ also triggers
                                   ▼
                           ┌──────────────┐
                           │     n8n      │
                           │   Webhook    │
                           │ (Direct HTTP)│
                           └──────────────┘
```

## 🗄️ Database Entities

```
┌────────────┐
│  Tenants   │
│────────────│
│ id (PK)    │──┐
│ company    │  │
│ phone_#_id │  │
│ api_key    │  │
└────────────┘  │
                │
    ┌───────────┴────────────┐
    │                        │
    ▼                        ▼
┌────────────┐       ┌──────────────┐
│ Customers  │       │Conversations │
│────────────│       │──────────────│
│ id (PK)    │──┐    │ id (PK)      │──┐
│ tenant_id  │  │    │ tenant_id    │  │
│ phone_#    │  │    │ customer_id  │  │
│ wa_user_id │  │    │ status       │  │
└────────────┘  │    │ last_msg_at  │  │
                │    └──────────────┘  │
                │                      │
                └──────────┬───────────┘
                           │
                           ▼
                   ┌────────────┐
                   │  Messages  │
                   │────────────│
                   │ id (PK)    │
                   │ conv_id    │
                   │ tenant_id  │
                   │ wa_msg_id  │
                   │ type       │
                   │ direction  │
                   │ content    │
                   │ media_url  │
                   │ timestamp  │
                   └────────────┘
```

## 🏗️ Arquitectura Hexagonal en Acción

```
                    ┌──────────────────┐
                    │    Bootstrap     │
                    │  (wires all up)  │
                    └────────┬─────────┘
                             │
        ┌────────────────────┼────────────────────┐
        │                    │                    │
        ▼                    ▼                    ▼
┌───────────────┐   ┌────────────────┐   ┌────────────────┐
│ Adapter IN    │   │  Application   │   │  Adapter OUT   │
│  (Web/REST)   │──>│  (Use Cases)   │──>│ (Persistence & │
│               │   │                │   │   External)    │
│ - Controller  │   │ - Service      │   │ - JPA Repos    │
│ - DTO/Mapper  │   │ - Commands     │   │ - HTTP Clients │
└───────────────┘   └────────┬───────┘   └────────────────┘
                             │
                             │ depends on
                             ▼
                    ┌──────────────────┐
                    │     Domain       │
                    │  (Pure Logic)    │
                    │                  │
                    │ - Entities       │
                    │ - Value Objects  │
                    │ - Ports          │
                    │   (interfaces)   │
                    │ - Events         │
                    └──────────────────┘
```

## 🎯 Key Ports (Interfaces)

### Input Ports (Use Cases)
- `HandleIncomingMessageUseCase`

### Output Ports (implemented by Adapters)
- `TenantRepositoryPort` → TenantRepositoryAdapter (JPA)
- `CustomerRepositoryPort` → CustomerRepositoryAdapter (JPA)
- `ConversationRepositoryPort` → ConversationRepositoryAdapter (JPA)
- `MessageRepositoryPort` → MessageRepositoryAdapter (JPA)
- `WhatsAppClientPort` → MetaWhatsAppClient (HTTP)
- `N8nClientPort` → N8nWebhookClient (HTTP)
- `EventPublisherPort` → RabbitEventPublisher (AMQP)

## 📊 Request/Response Example

### Request (WhatsApp Webhook)
```json
{
  "object": "whatsapp_business_account",
  "entry": [{
    "changes": [{
      "value": {
        "metadata": {
          "phone_number_id": "phone_number_id_demo_123"
        },
        "contacts": [{
          "wa_id": "1234567890",
          "profile": {"name": "John Doe"}
        }],
        "messages": [{
          "from": "1234567890",
          "id": "wamid.123",
          "timestamp": "1640000000",
          "type": "text",
          "text": {"body": "Hello!"}
        }]
      }
    }]
  }]
}
```

### Response
```json
{
  "messageId": "uuid-1234-5678",
  "conversationId": "uuid-abcd-efgh",
  "customerId": "uuid-9999-8888",
  "status": "processed",
  "message": "Message received successfully"
}
```

### Event Published to RabbitMQ
```json
{
  "eventType": "incoming_message",
  "messageId": "uuid-1234-5678",
  "conversationId": "uuid-abcd-efgh",
  "customerId": "uuid-9999-8888",
  "tenantId": "uuid-tenant-id",
  "content": "Hello!",
  "messageType": "TEXT",
  "timestamp": "2024-01-01T12:00:00Z"
}
```

---

**Este diagrama muestra el flujo completo del caso de uso principal!** 🎯
