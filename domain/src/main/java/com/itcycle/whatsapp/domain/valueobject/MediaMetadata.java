package com.itcycle.whatsapp.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MediaMetadata - holds metadata about media files (audio, image, document, video).
 * Immutable value object following DDD principles.
 * 
 * This metadata is used to download and process media files from WhatsApp Cloud API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaMetadata {
    
    /**
     * WhatsApp media ID (used to retrieve download URL)
     */
    private String mediaId;
    
    /**
     * MIME type of the media (e.g., audio/ogg, image/jpeg, application/pdf)
     */
    private String mimeType;
    
    /**
     * Original filename (if available)
     */
    private String filename;
    
    /**
     * File size in bytes (if available)
     */
    private Long fileSize;
    
    /**
     * SHA256 hash of the file (for verification)
     */
    private String sha256;
    
    /**
     * Download URL from Meta API (temporary, expires after ~5 minutes)
     */
    private String downloadUrl;
    
    /**
     * Caption or description attached to the media
     */
    private String caption;
    
    /**
     * Check if this is an audio file
     */
    public boolean isAudio() {
        return mimeType != null && mimeType.startsWith("audio/");
    }
    
    /**
     * Check if this is an image file
     */
    public boolean isImage() {
        return mimeType != null && mimeType.startsWith("image/");
    }
    
    /**
     * Check if this is a document (PDF, Word, etc.)
     */
    public boolean isDocument() {
        return mimeType != null && (
            mimeType.equals("application/pdf") ||
            mimeType.contains("document") ||
            mimeType.contains("msword") ||
            mimeType.contains("officedocument")
        );
    }
    
    /**
     * Check if this is a video file
     */
    public boolean isVideo() {
        return mimeType != null && mimeType.startsWith("video/");
    }
    
    /**
     * Get a human-readable media type
     */
    public String getMediaType() {
        if (isAudio()) return "AUDIO";
        if (isImage()) return "IMAGE";
        if (isDocument()) return "DOCUMENT";
        if (isVideo()) return "VIDEO";
        return "UNKNOWN";
    }
}
