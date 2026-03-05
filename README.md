# iTCycle WhatsApp Hub

B2B WhatsApp conversational automation platform built with **Hexagonal Architecture** (Ports & Adapters).

## 🏗️ Architecture

This project implements **Hexagonal Architecture** (also known as Ports and Adapters) with a clean separation of concerns:

```
┌─────────────────────────────────────────────────────────┐
│                     Bootstrap Module                     │
│         (Main Application & Configuration)              │
└─────────────────────────────────────────────────────────┘
                            │
        ┌───────────────────┼───────────────────┐
        │                   │                   │
┌───────▼───────┐   ┌──────▼───────┐   ┌──────▼────────┐
│  Adapters-In  │   │ Application  │   │ Adapters-Out  │
│   (Web/REST)  │───│  Use Cases   │───│(Persistence & │
│               │   │              │   │   External)   │
└───────────────┘   └──────┬───────┘   └───────────────┘
                           │
                    ┌──────▼───────┐
                    │    Domain    │
                    │(Entities &   │
                    │    Ports)    │
                    └──────────────┘
```

### Module Structure

- **domain**: Core business entities, value objects, and port interfaces (no framework dependencies)
- **application**: Use cases and business logic orchestration
- **adapters-in-web**: REST controllers and DTOs
- **adapters-out-persistence**: JPA repositories and entities
- **adapters-out-external**: External API clients (WhatsApp, n8n)
- **bootstrap**: Main Spring Boot application and configurations

## 🚀 Features

- ✅ Receive WhatsApp messages via webhook (Meta Cloud API)
- ✅ Store conversations and messages in PostgreSQL
- ✅ Automatic customer identification and conversation management
- ✅ Event-driven architecture with RabbitMQ
- ✅ n8n workflow automation integration
- ✅ JWT authentication for admin endpoints
- ✅ OpenAPI/Swagger documentation
- ✅ Actuator health checks and metrics
- ✅ Flyway database migrations
- ✅ Multi-tenant support

## 📋 Prerequisites

- Java 21
- Maven 3.9+
- Docker & Docker Compose
- PostgreSQL 16 (or use Docker)
- RabbitMQ (or use Docker)

## 🛠️ Local Setup

### 1. Start Infrastructure Services

```bash
docker-compose up -d postgres rabbitmq redis n8n
```

This starts:
- PostgreSQL on port 5432
- RabbitMQ on ports 5672 (AMQP) and 15672 (Management UI)
- Redis on port 6379
- n8n on port 5678

### 2. Configure Environment Variables

Create a `.env` file or set environment variables:

```bash
# WhatsApp Configuration
WHATSAPP_ACCESS_TOKEN=your_meta_access_token
WHATSAPP_VERIFY_TOKEN=your_webhook_verify_token

# JWT Secret
JWT_SECRET=your_secret_key_here

# n8n Configuration (optional)
N8N_WEBHOOK_URL=http://localhost:5678/webhook
N8N_ENABLED=true
```

### 3. Build the Project

```bash
mvn clean install
```

### 4. Run the Application

```bash
cd bootstrap
mvn spring-boot:run
```

Or run the JAR:

```bash
java -jar bootstrap/target/bootstrap-0.0.1-SNAPSHOT.jar
```

The application will start on **http://localhost:8080**

## 📚 API Documentation

Once the application is running, access the interactive API documentation:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

### Key Endpoints

| Endpoint | Method | Description | Auth |
|----------|--------|-------------|------|
| `/webhooks/whatsapp` | GET | Webhook verification (Meta) | Public |
| `/webhooks/whatsapp` | POST | Receive WhatsApp messages | Public |
| `/actuator/health` | GET | Health check | Public |
| `/actuator/info` | GET | Application info | Public |

## 🔐 Security

- **Public endpoints**: `/webhooks/**`, `/swagger-ui/**`, `/actuator/health`
- **Protected endpoints**: `/admin/**`, `/actuator/**` (except health)
- **Authentication**: JWT Bearer tokens (implementation pending for admin endpoints)

## 🗄️ Database Schema

The application uses Flyway for database migrations. Tables:

- `tenants`: Companies using the platform
- `customers`: End-users (WhatsApp contacts)
- `conversations`: Conversation threads
- `messages`: Individual messages

A sample tenant is created automatically for testing:
- **Phone Number ID**: `phone_number_id_demo_123`
- **API Key**: `demo_api_key_itcycle_2024_change_in_production`

## 🧪 Testing

### Run Unit Tests

```bash
mvn test
```

### Run Integration Tests

```bash
mvn verify
```

Integration tests use Testcontainers for PostgreSQL.

### Test Webhook Locally

Use ngrok to expose your local server:

```bash
ngrok http 8080
```

Configure the ngrok URL in Meta WhatsApp Business settings.

## 🐰 RabbitMQ Configuration

The application publishes events to RabbitMQ:

- **Exchange**: `whatsapp.events` (topic)
- **Queue**: `whatsapp.incoming.messages`
- **Routing Key**: `message.incoming`

Access RabbitMQ Management: http://localhost:15672
- Username: `guest`
- Password: `guest`

## 🔄 n8n Workflow Integration

To trigger workflows on incoming messages:

1. Access n8n: http://localhost:5678
2. Login: `admin` / `admin`
3. Create a workflow with a Webhook trigger
4. Set webhook URL to match: `http://localhost:5678/webhook/message-received`
5. The application will automatically trigger this workflow on incoming messages

Example n8n payload:
```json
{
  "eventType": "incoming_message",
  "messageId": "uuid",
  "conversationId": "uuid",
  "customerId": "uuid",
  "tenantId": "uuid",
  "content": "message text",
  "messageType": "TEXT",
  "timestamp": "2024-01-01T12:00:00Z"
}
```

## 📦 Build & Deploy

### Build JAR

```bash
mvn clean package -DskipTests
```

The executable JAR will be in `bootstrap/target/`

### Docker Build (Optional)

Create a `Dockerfile` in the root:

```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY bootstrap/target/bootstrap-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Build and run:

```bash
docker build -t itcycle-whatsapp-hub .
docker run -p 8080:8080 --env-file .env itcycle-whatsapp-hub
```

## 🔧 Configuration

Key configuration properties in `application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/whatsapp_hub
  
whatsapp:
  meta:
    api:
      url: https://graph.facebook.com/v18.0
    access:
      token: ${WHATSAPP_ACCESS_TOKEN}

n8n:
  webhook:
    base:
      url: ${N8N_WEBHOOK_URL}
    enabled: true
```

## 📝 Development Notes

### Adding New Use Cases

1. Define interface in `application/port/in`
2. Implement service in `application/service`
3. Create controller in `adapters-in-web/controller`

### Adding New External Integration

1. Define port interface in `domain/port/out`
2. Implement adapter in `adapters-out-external`
3. Configure in `bootstrap/config`

### Database Migrations

Create new migration in `bootstrap/src/main/resources/db/migration/`:

```sql
-- V6__description.sql
ALTER TABLE ...
```

## 🤝 Contributing

1. Follow hexagonal architecture principles
2. Keep domain module free of framework dependencies
3. Use ports (interfaces) for all external dependencies
4. Write meaningful commit messages
5. Add tests for new features

## 📄 License

Proprietary - iTCycle

## 👥 Team

iTCycle Development Team

---

**Built with ❤️ using Spring Boot 3.5 and Hexagonal Architecture**
