# 🔑 API Keys Setup

Este proyecto requiere las siguientes API keys para funcionar correctamente.

## ⚠️ IMPORTANTE: No subir API keys al repositorio

**NUNCA** incluyas las API keys reales en el código que subes a GitHub.

---

## 📋 API Keys Necesarias

### 1. **Groq API Key**
- **Dónde obtenerla**: https://console.groq.com/keys
- **Dónde usarla**: 
  - En n8n: Workflow → Nodo HTTP Request para Groq → Header `Authorization: Bearer YOUR_KEY`
  - Archivos: `N8N_WORKFLOW_GROQ_FREE.json`, `N8N_WORKFLOW_AI_COMPLETE_FIXED.json`

### 2. **WhatsApp Business API (Meta)**
- **Dónde obtenerla**: https://developers.facebook.com/apps
- **Configuración**:
  - Access Token: En `src/main/resources/application.yaml` → `whatsapp.meta.access-token`
  - Phone Number ID: En `src/main/resources/application.yaml` → `whatsapp.meta.phone-number-id`

---

## 🛠️ Configuración en n8n

### Workflows que requieren API Keys:

1. **N8N_WORKFLOW_GROQ_FREE.json**
   - Línea 127: Reemplazar `YOUR_GROQ_API_KEY_HERE` con tu API key real
   
2. **N8N_WORKFLOW_AI_COMPLETE_FIXED.json**
   - Buscar todos los nodos de Groq AI Agent
   - Configurar credenciales de Groq en n8n Settings → Credentials

---

## 📝 Pasos para Configurar

### Opción 1: Usando n8n UI (Recomendado)
1. Importa el workflow JSON a n8n
2. Ve a **Settings** → **Credentials**
3. Crea nueva credencial tipo **HTTP Header Auth**
4. Nombre: `Groq API`
5. Header: `Authorization`
6. Value: `Bearer gsk_TU_API_KEY_AQUI`
7. Asigna esta credencial a todos los nodos Groq

### Opción 2: Editando el JSON directamente
1. Copia el archivo workflow
2. Busca: `YOUR_GROQ_API_KEY_HERE`
3. Reemplaza con tu API key real
4. **NO subas este archivo a Git**
5. Renombra a `*_WITH_KEYS.json` (está en .gitignore)

---

## 🔒 Seguridad

### Archivos que NO deben subirse a Git:
- ❌ `*_WITH_KEYS.json`
- ❌ `.env` (si lo usas)
- ❌ Cualquier archivo con API keys reales
- ❌ `application.yaml` con tokens reales

### Archivos seguros para Git:
- ✅ `N8N_WORKFLOW_GROQ_FREE.json` (con placeholders)
- ✅ `application.yaml` (con valores de ejemplo)
- ✅ Este archivo README

---

## 🚀 Flujo de Trabajo Recomendado

1. **Desarrollo local**:
   ```bash
   # Copia el workflow template
   cp N8N_WORKFLOW_GROQ_FREE.json N8N_WORKFLOW_WITH_KEYS.json
   
   # Edita y agrega tus API keys
   notepad N8N_WORKFLOW_WITH_KEYS.json
   
   # Este archivo NO se subirá a Git (está en .gitignore)
   ```

2. **Configurar application.yaml**:
   ```yaml
   whatsapp:
     meta:
       access-token: "TU_TOKEN_AQUI"  # NO subir a Git
       phone-number-id: "TU_PHONE_ID"  # NO subir a Git
   ```

3. **Variables de entorno** (alternativa):
   ```bash
   export GROQ_API_KEY="gsk_..."
   export WHATSAPP_ACCESS_TOKEN="EAA..."
   ```

---

## 📞 API Keys por Entorno

| Entorno | Groq API | WhatsApp Token | Dónde configurar |
|---------|----------|----------------|------------------|
| Desarrollo | n8n local | Mock (no necesita) | n8n Settings |
| Staging | n8n cloud | Test number | Variables entorno |
| Producción | n8n cloud | Número real | Secrets Manager |

---

## ❓ Troubleshooting

### "Push declined due to repository rule violations"
- Has intentado subir una API key real
- Solución: Reemplaza con placeholder y vuelve a hacer commit

### "401 Unauthorized" en Groq
- Tu API key es inválida o expiró
- Solución: Genera una nueva en https://console.groq.com/keys

### "The requested webhook is not registered" en WhatsApp
- No configuraste el webhook en Meta
- Solución: Ve a https://developers.facebook.com/apps → Tu App → WhatsApp → Configuration

---

## 📚 Links Útiles

- Groq Console: https://console.groq.com
- Meta Developers: https://developers.facebook.com
- n8n Credentials: https://docs.n8n.io/credentials/
- GitHub Secret Scanning: https://docs.github.com/code-security/secret-scanning

---

**Última actualización**: Febrero 24, 2026
