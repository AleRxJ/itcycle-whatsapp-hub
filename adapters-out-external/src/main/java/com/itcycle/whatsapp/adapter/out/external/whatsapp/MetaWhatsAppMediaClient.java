package com.itcycle.whatsapp.adapter.out.external.whatsapp;

import com.itcycle.whatsapp.domain.port.out.WhatsAppMediaPort;
import com.itcycle.whatsapp.domain.valueobject.MediaMetadata;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.UUID;

/**
 * MetaWhatsAppMediaClient - retrieves media files from WhatsApp Cloud API.
 * 
 * Implements port to download audio, images, documents, and videos from Meta's API.
 * Media URLs are temporary and expire after ~5 minutes.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class MetaWhatsAppMediaClient implements WhatsAppMediaPort {
    
    private final RestTemplate restTemplate;
    
    @Value("${whatsapp.meta.api.url:https://graph.facebook.com/v18.0}")
    private String apiUrl;
    
    @Value("${whatsapp.meta.access.token:}")
    private String accessToken;
    
    @Override
    public String getMediaDownloadUrl(UUID tenantId, String mediaId) {
        log.info("Retrieving download URL for media: {}", mediaId);
        
        String url = String.format("%s/%s", apiUrl, mediaId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        
        HttpEntity<Void> request = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                request, 
                Map.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                String downloadUrl = (String) body.get("url");
                
                if (downloadUrl != null) {
                    log.debug("Retrieved download URL for media {}: {}", mediaId, downloadUrl);
                    return downloadUrl;
                } else {
                    log.warn("No URL found in response for media {}", mediaId);
                    throw new RuntimeException("No download URL in Meta API response");
                }
            }
            
            log.error("Failed to retrieve media URL. Status: {}", response.getStatusCode());
            throw new RuntimeException("Failed to retrieve media from Meta API");
            
        } catch (Exception e) {
            log.error("Error retrieving media download URL for mediaId: {}", mediaId, e);
            throw new RuntimeException("Failed to retrieve media from Meta API", e);
        }
    }
    
    @Override
    public MediaMetadata getMediaMetadata(UUID tenantId, String mediaId) {
        log.info("Retrieving metadata for media: {}", mediaId);
        
        String url = String.format("%s/%s", apiUrl, mediaId);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        
        HttpEntity<Void> request = new HttpEntity<>(headers);
        
        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                url, 
                HttpMethod.GET, 
                request, 
                Map.class
            );
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> body = response.getBody();
                
                return MediaMetadata.builder()
                        .mediaId(mediaId)
                        .downloadUrl((String) body.get("url"))
                        .mimeType((String) body.get("mime_type"))
                        .sha256((String) body.get("sha256"))
                        .fileSize(body.get("file_size") != null ? 
                                  Long.valueOf(body.get("file_size").toString()) : null)
                        .build();
            }
            
            log.error("Failed to retrieve media metadata. Status: {}", response.getStatusCode());
            throw new RuntimeException("Failed to retrieve media metadata from Meta API");
            
        } catch (Exception e) {
            log.error("Error retrieving media metadata for mediaId: {}", mediaId, e);
            throw new RuntimeException("Failed to retrieve media metadata from Meta API", e);
        }
    }
}
