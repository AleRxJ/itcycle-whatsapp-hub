package com.itcycle.whatsapp.adapter.out.external.n8n;

import com.itcycle.whatsapp.domain.port.out.N8nClientPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * N8nWebhookClient - n8n workflow automation client.
 * Triggers n8n workflows via webhook URLs.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class N8nWebhookClient implements N8nClientPort {
    
    private final RestTemplate restTemplate;
    
    @Value("${n8n.webhook.base.url:http://localhost:5678/webhook}")
    private String baseUrl;
    
    @Value("${n8n.webhook.enabled:true}")
    private boolean enabled;
    
    @Override
    public String triggerFlow(String flowKey, Map<String, Object> payload) {
        if (!enabled) {
            log.warn("n8n integration is disabled. Skipping flow trigger: {}", flowKey);
            return "disabled";
        }
        
        log.info("Triggering n8n workflow: {}", flowKey);
        
        String url = String.format("%s/%s", baseUrl, flowKey);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            
            log.info("n8n workflow {} triggered successfully. Status: {}", flowKey, response.getStatusCode());
            return response.getBody();
            
        } catch (Exception e) {
            log.error("Error triggering n8n workflow: {}", flowKey, e);
            // Don't fail the main operation if n8n call fails
            return "error: " + e.getMessage();
        }
    }
    
    @Override
    public Map<String, Object> triggerFlowSync(String flowKey, Map<String, Object> payload) {
        if (!enabled) {
            log.warn("n8n integration is disabled. Skipping flow trigger: {}", flowKey);
            return Map.of("status", "disabled");
        }
        
        log.info("Triggering n8n workflow (sync): {}", flowKey);
        
        String url = String.format("%s/%s", baseUrl, flowKey);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            
            log.info("n8n workflow {} completed. Status: {}", flowKey, response.getStatusCode());
            return response.getBody();
            
        } catch (Exception e) {
            log.error("Error triggering n8n workflow: {}", flowKey, e);
            return Map.of("error", e.getMessage());
        }
    }
}
