package com.itcycle.whatsapp.domain.port.out;

import com.itcycle.whatsapp.domain.valueobject.MediaMetadata;

import java.util.UUID;

/**
 * WhatsAppMediaPort - port for retrieving media files from WhatsApp Cloud API.
 * 
 * This port abstracts the integration with Meta's API to download media files.
 * Implementations handle authentication, rate limiting, and error handling.
 */
public interface WhatsAppMediaPort {
    
    /**
     * Retrieve the download URL for a media file from WhatsApp Cloud API.
     * 
     * The URL is temporary and expires after ~5 minutes.
     * 
     * @param tenantId Tenant identifier (to get correct access token)
     * @param mediaId WhatsApp media ID
     * @return Download URL for the media file
     * @throws RuntimeException if media retrieval fails
     */
    String getMediaDownloadUrl(UUID tenantId, String mediaId);
    
    /**
     * Retrieve complete metadata about a media file (includes download URL).
     * 
     * @param tenantId Tenant identifier
     * @param mediaId WhatsApp media ID
     * @return MediaMetadata with all available information
     * @throws RuntimeException if media retrieval fails
     */
    MediaMetadata getMediaMetadata(UUID tenantId, String mediaId);
}
