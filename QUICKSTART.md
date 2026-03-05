# Quick Start Guide - iTCycle WhatsApp Hub

## 🚀 Quickstart (5 minutes)

### 1. Start Infrastructure

```bash
docker-compose up -d postgres rabbitmq redis
```

### 2. Build & Run

```bash
mvn clean install
cd bootstrap
mvn spring-boot:run
```

### 3. Verify Setup

- **Health Check**: http://localhost:8080/actuator/health
- **API Docs**: http://localhost:8080/swagger-ui.html
- **RabbitMQ UI**: http://localhost:15672 (guest/guest)

## 📬 Test Webhook

### 1. Webhook Verification (GET)

```bash
curl "http://localhost:8080/webhooks/whatsapp?hub.mode=subscribe&hub.challenge=test123&hub.verify_token=itcycle_whatsapp_hub_verify_token"
```

Expected response: `test123`

### 2. Receive Message (POST)

```bash
curl -X POST http://localhost:8080/webhooks/whatsapp \
  -H "Content-Type: application/json" \
  -d '{
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
          "profile": {"name": "Test User"},
          "wa_id": "1234567890"
        }],
        "messages": [{
          "from": "1234567890",
          "id": "wamid.test123",
          "timestamp": "1640000000",
          "type": "text",
          "text": {"body": "Hello from test!"}
        }]
      },
      "field": "messages"
    }]
  }]
}'
```

Expected: 200 OK with `messageId`, `conversationId`, `customerId`

## 🗄️ Check Database

```bash
docker exec -it whatsapp-hub-postgres psql -U postgres -d whatsapp_hub

# Inside psql:
\dt                           # List tables
SELECT * FROM tenants;        # View tenants
SELECT * FROM customers;      # View customers
SELECT * FROM conversations;  # View conversations
SELECT * FROM messages;       # View messages
```

## 🐰 Check RabbitMQ

1. Open http://localhost:15672
2. Login: `guest` / `guest`
3. Check Queues → `whatsapp.incoming.messages`
4. View messages published

## 🔄 Configure n8n (Optional)

```bash
docker-compose up -d n8n
```

1. Open http://localhost:5678
2. Login: `admin` / `admin`
3. Create workflow with Webhook trigger
4. Set URL: `/webhook/message-received`
5. Activate workflow

Messages will automatically trigger n8n!

## 🧪 Run Tests

```bash
# Unit tests
mvn test

# Integration tests (with Testcontainers)
mvn verify
```

## 📦 Module Overview

```
itcycle-whatsapp-hub/
├── domain/              # Pure business logic (no dependencies)
├── application/         # Use cases
├── adapters-in-web/     # REST controllers
├── adapters-out-persistence/  # JPA repositories
├── adapters-out-external/     # WhatsApp & n8n clients
└── bootstrap/           # Main app & config
```

## 🎯 Next Steps

1. **Configure WhatsApp**: Set `WHATSAPP_ACCESS_TOKEN` environment variable
2. **Webhook URL**: Use ngrok for local testing: `ngrok http 8080`
3. **Production**: Update security configs and secrets
4. **Monitoring**: Configure Actuator metrics and alerts

## ⚙️ Environment Variables

```bash
# Required for WhatsApp integration
export WHATSAPP_ACCESS_TOKEN="your_meta_token"
export WHATSAPP_VERIFY_TOKEN="your_verify_token"

# Optional
export JWT_SECRET="your_jwt_secret"
export N8N_WEBHOOK_URL="http://localhost:5678/webhook"
```

## 🆘 Troubleshooting

### Port Already in Use

```bash
# Check what's using port 8080
netstat -ano | findstr :8080

# Kill the process or change port in application.yaml
```

### Database Connection Failed

```bash
# Ensure PostgreSQL is running
docker ps | grep postgres

# Check logs
docker logs whatsapp-hub-postgres
```

### RabbitMQ Connection Failed

```bash
# Check RabbitMQ status
docker logs whatsapp-hub-rabbitmq

# Restart if needed
docker-compose restart rabbitmq
```

## 📖 More Info

See [README.md](README.md) for complete documentation.

---

**Happy Coding! 🚀**
