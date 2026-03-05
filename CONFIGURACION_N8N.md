# Configuración de n8n para iTCycle WhatsApp Hub

## 🎯 Objetivo
Configurar un workflow en n8n que:
1. Consuma mensajes de RabbitMQ
2. Procese el mensaje (aplique lógica, IA, etc.)
3. Envíe respuesta a WhatsApp

## 📝 Paso 1: Acceder a n8n

1. Abre tu navegador en: http://localhost:5678
2. Si es la primera vez, crea tu cuenta de n8n (solo local, no se envía a ningún servidor)

## 🔧 Paso 2: Crear Workflow para WhatsApp

### 2.1 Crear Nuevo Workflow

1. Click en "**Add workflow**"
2. Nombra el workflow: "WhatsApp Message Processor"

### 2.2 Agregar Trigger - RabbitMQ

1. Click en el botón "**+**" para agregar nodo
2. Busca "**RabbitMQ Trigger**"
3. Configura:
   ```
   Connection:
     - Host: rabbitmq (o localhost si n8n no está en Docker)
     - Port: 5672
     - User: guest
     - Password: guest
     - Vhost: /
   
   Queue Name: whatsapp.incoming.messages
   Options:
     - Auto Acknowledge: false (para control manual)
   ```

### 2.3 Agregar Nodo de Procesamiento

1. Agrega nodo "**Function**"
2. Dale nombre: "Process Message"
3. Código de ejemplo:

```javascript
// Obtener datos del mensaje de RabbitMQ
const message = $input.item.json;

// Log para debugging
console.log('Mensaje recibido:', message);

// Extraer información
const customerPhone = message.from;
const messageText = message.text;
const tenantPhoneId = message.tenantPhoneId;

// AQUÍ PUEDES AGREGAR TU LÓGICA:
// - Llamar a un servicio de IA (OpenAI, etc.)
// - Aplicar reglas de negocio
// - Consultar base de datos
// - Etc.

// Ejemplo simple: respuesta automática
let responseText = '';

if (messageText.toLowerCase().includes('pedido')) {
  responseText = '¡Hola! Gracias por contactarnos. Un agente revisará tu pedido y te responderá pronto.';
} else if (messageText.toLowerCase().includes('ayuda')) {
  responseText = 'Estoy aquí para ayudarte. ¿Qué necesitas?';
} else {
  responseText = 'Gracias por tu mensaje. Te responderemos pronto.';
}

// Preparar datos para enviar a WhatsApp
return {
  customerPhone: customerPhone,
  responseText: responseText,
  tenantPhoneId: tenantPhoneId,
  originalMessage: message
};
```

### 2.4 Agregar Nodo HTTP Request - Enviar a WhatsApp

1. Agrega nodo "**HTTP Request**"
2. Dale nombre: "Send WhatsApp Message"
3. Configura:

```
Method: POST
URL: http://host.docker.internal:8080/api/whatsapp/send
  (o http://localhost:8080/api/whatsapp/send si n8n no está en Docker)

Authentication: None (por ahora)

Headers:
  Content-Type: application/json

Body (JSON):
{
  "phoneNumberId": "{{ $json.tenantPhoneId }}",
  "to": "{{ $json.customerPhone }}",
  "message": "{{ $json.responseText }}"
}
```

### 2.5 Activar Workflow

1. En la esquina superior derecha, activa el workflow (toggle switch)
2. Guarda el workflow (Ctrl + S)

## 🚀 Paso 3: Crear Endpoint para Enviar Mensajes

Actualmente tu aplicación solo **recibe** mensajes. Necesitas crear un endpoint para **enviar** mensajes.

### 3.1 Crear SendMessageController

Archivo: `adapters-in-web/src/main/java/com/itcycle/whatsapp/adapter/in/web/controller/SendMessageController.java`

```java
package com.itcycle.whatsapp.adapter.in.web.controller;

import com.itcycle.whatsapp.domain.port.out.WhatsAppClientPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/whatsapp/send")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "WhatsApp Send", description = "Endpoints for sending WhatsApp messages")
public class SendMessageController {
    
    private final WhatsAppClientPort whatsAppClient;
    
    @PostMapping
    @Operation(summary = "Send WhatsApp message", description = "Send a text message through WhatsApp Cloud API")
    public ResponseEntity<Map<String, String>> sendMessage(@RequestBody SendMessageRequest request) {
        
        log.info("Sending WhatsApp message to: {}", request.getTo());
        
        String messageId = whatsAppClient.sendTextMessage(
            request.getPhoneNumberId(),
            request.getTo(),
            request.getMessage()
        );
        
        return ResponseEntity.ok(Map.of(
            "status", "sent",
            "messageId", messageId,
            "to", request.getTo()
        ));
    }
    
    @lombok.Data
    public static class SendMessageRequest {
        private String phoneNumberId;
        private String to;
        private String message;
    }
}
```

### 3.2 Actualizar SecurityConfig

Agrega el nuevo endpoint como público en `SecurityConfig.java`:

```java
.requestMatchers("/webhooks/**").permitAll()
.requestMatchers("/api/whatsapp/send").permitAll()  // AGREGAR ESTA LÍNEA
.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
```

## 🧪 Paso 4: Probar el Flujo Completo

### 4.1 Verificar que todo esté corriendo:

```powershell
# Verificar Docker
docker-compose ps

# Verificar aplicación
Invoke-RestMethod -Uri "http://localhost:8080/actuator/health"

# Verificar n8n
Invoke-RestMethod -Uri "http://localhost:5678"
```

### 4.2 Enviar mensaje de prueba:

```powershell
$body = @'
{
  "object": "whatsapp_business_account",
  "entry": [{
    "changes": [{
      "value": {
        "messaging_product": "whatsapp",
        "metadata": {
          "phone_number_id": "+573001234567"
        },
        "contacts": [{
          "wa_id": "573219876543",
          "profile": { "name": "Test User" }
        }],
        "messages": [{
          "id": "wamid.TEST_123",
          "from": "573219876543",
          "timestamp": "1703001234",
          "type": "text",
          "text": { "body": "Necesito ayuda con mi pedido" }
        }]
      }
    }]
  }]
}
'@

Invoke-RestMethod -Uri "http://localhost:8080/webhooks/whatsapp" `
  -Method POST `
  -Body $body `
  -ContentType "application/json"
```

### 4.3 Verificar el flujo:

1. **PostgreSQL**: Verifica que el mensaje se guardó
   ```powershell
   docker exec -it whatsapp-hub-postgres psql -U postgres -d whatsapp_hub -c "SELECT * FROM messages ORDER BY created_at DESC LIMIT 1;"
   ```

2. **RabbitMQ**: Verifica la cola
   - Abre: http://localhost:15672
   - User: guest / Password: guest
   - Ve a "Queues" → "whatsapp.incoming.messages"

3. **n8n**: Verifica ejecuciones
   - Abre: http://localhost:5678
   - Ve a "Executions" en tu workflow
   - Deberías ver la ejecución con estado "success"

## 🌍 Paso 5: Conectar con WhatsApp Real

Para conectar con WhatsApp real necesitas:

### 5.1 Obtener Credenciales de Meta

1. Ve a: https://developers.facebook.com
2. Crea una aplicación
3. Agrega el producto "WhatsApp Business API"
4. Obtén:
   - **Phone Number ID**
   - **WhatsApp Business Account ID**
   - **Access Token**
   - **Webhook Verify Token**

### 5.2 Configurar tu aplicación

Actualiza `application.yaml`:

```yaml
whatsapp:
  cloud-api:
    base-url: https://graph.facebook.com/v18.0
    access-token: ${WHATSAPP_ACCESS_TOKEN}  # <-- Token real de Meta
    business-account-id: ${WHATSAPP_BUSINESS_ACCOUNT_ID}
  webhook:
    verify-token: ${WHATSAPP_VERIFY_TOKEN}  # <-- Token de verificación
```

### 5.3 Exponer tu webhook públicamente

Meta necesita acceder a tu webhook. Opciones:

**Opción A: ngrok (desarrollo)**
```powershell
# Instalar ngrok: https://ngrok.com/download
ngrok http 8080
```

**Opción B: Despliegue en cloud (producción)**
- AWS, Azure, Google Cloud
- O servicios como Railway, Render, Fly.io

### 5.4 Configurar webhook en Meta

1. En el panel de Meta Developers
2. WhatsApp → Configuration
3. Webhook URL: `https://tu-dominio.com/webhooks/whatsapp`
4. Verify Token: el que configuraste en `WHATSAPP_VERIFY_TOKEN`
5. Suscríbete a mensajes: `messages`

## 📊 Resumen del Flujo

```
┌─────────────┐
│  WhatsApp   │ Usuario envía mensaje
│   (Cliente) │
└──────┬──────┘
       │
       ↓
┌─────────────┐
│ Meta Cloud  │ Meta recibe el mensaje
│     API     │
└──────┬──────┘
       │
       ↓ POST /webhooks/whatsapp
┌─────────────┐
│ Tu Webhook  │ 1. Guarda en PostgreSQL
│   (Spring)  │ 2. Publica en RabbitMQ
└──────┬──────┘
       │
       ↓
┌─────────────┐
│  RabbitMQ   │ Cola: whatsapp.incoming.messages
└──────┬──────┘
       │
       ↓ Consume mensaje
┌─────────────┐
│    n8n      │ 1. Procesa mensaje
│  Workflow   │ 2. Aplica lógica/IA
└──────┬──────┘
       │
       ↓ POST /api/whatsapp/send
┌─────────────┐
│ Tu API Send │ Llama a Meta Cloud API
│   (Spring)  │
└──────┬──────┘
       │
       ↓ POST a Meta
┌─────────────┐
│ Meta Cloud  │ Envía mensaje al cliente
│     API     │
└──────┬──────┘
       │
       ↓
┌─────────────┐
│  WhatsApp   │ Usuario recibe respuesta
│   (Cliente) │
└─────────────┘
```

## 🎯 Próximos Pasos Recomendados

1. ✅ **Crear SendMessageController** (para que n8n pueda enviar mensajes)
2. ✅ **Actualizar SecurityConfig** (permitir /api/whatsapp/send)
3. ✅ **Configurar workflow en n8n** (según esta guía)
4. ✅ **Probar flujo local** (sin WhatsApp real)
5. ⏳ **Obtener credenciales de Meta** (para WhatsApp real)
6. ⏳ **Exponer webhook** (ngrok o cloud)
7. ⏳ **Integrar con WhatsApp real**

## 💡 Ideas de Mejora

- **IA**: Integrar OpenAI/Claude en n8n para respuestas inteligentes
- **CRM**: Conectar con Salesforce, HubSpot, etc.
- **Base de Conocimiento**: RAG con documentos de tu negocio
- **Métricas**: Dashboards de conversaciones, tiempos de respuesta
- **Multi-tenancy**: Permitir múltiples empresas en la plataforma

## 🆘 Troubleshooting

### n8n no puede conectar a RabbitMQ
- Usa `rabbitmq` como hostname si n8n está en Docker
- Usa `localhost` si n8n está fuera de Docker

### n8n no puede llamar a Spring Boot
- Usa `host.docker.internal:8080` si n8n está en Docker
- Usa `localhost:8080` si n8n está fuera de Docker

### Mensajes no llegan a la cola
- Verifica que RabbitMQ esté habilitado en `application.yaml`
- Revisa logs de Spring Boot para errores de conexión
