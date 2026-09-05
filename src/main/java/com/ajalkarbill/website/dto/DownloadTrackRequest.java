package com.ajalkarbill.website.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DownloadTrackRequest {

    @NotNull(message = "Download ID is required")
    private Long downloadId;

    @Size(max = 64, message = "Visitor ID must be at most 64 characters")
    private String visitorId;

    @Size(max = 64, message = "Session ID must be at most 64 characters")
    private String sessionId;

    @Size(max = 100, message = "File version must be at most 100 characters")
    private String fileVersion;

    public DownloadTrackRequest() {
    }

    public Long getDownloadId() {
        return downloadId;
    }

    public void setDownloadId(Long downloadId) {
        this.downloadId = downloadId;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getFileVersion() {
        return fileVersion;
    }

    public void setFileVersion(String fileVersion) {
        this.fileVersion = fileVersion;
    }
}