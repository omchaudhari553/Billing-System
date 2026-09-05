package com.ajalkarbill.website.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class VisitorTrackRequest {

    @NotBlank(message = "Visitor ID is required")
    @Size(max = 64, message = "Visitor ID must be at most 64 characters")
    private String visitorId;

    @Size(max = 64, message = "Session ID must be at most 64 characters")
    private String sessionId;

    @Size(max = 500, message = "Page URL must be at most 500 characters")
    private String pageUrl;

    @Size(max = 1000, message = "User agent must be at most 1000 characters")
    private String userAgent;

    @Size(max = 50, message = "Device type must be at most 50 characters")
    private String deviceType;

    @Size(max = 50, message = "Operating system must be at most 50 characters")
    private String operatingSystem;

    @Size(max = 500, message = "Referrer must be at most 500 characters")
    private String referrer;

    public VisitorTrackRequest() {
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

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }
}