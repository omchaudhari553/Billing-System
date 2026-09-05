package com.ajalkarbill.website.dto;

import com.ajalkarbill.website.entity.WebsiteActivity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ActivityTrackRequest {

    @NotBlank(message = "Visitor ID is required")
    @Size(max = 64, message = "Visitor ID must be at most 64 characters")
    private String visitorId;

    @Size(max = 64, message = "Session ID must be at most 64 characters")
    private String sessionId;

    @NotNull(message = "Event type is required")
    private WebsiteActivity.ActivityEventType eventType;

    @Size(max = 500, message = "Page URL must be at most 500 characters")
    private String pageUrl;

    @Size(max = 200, message = "Page title must be at most 200 characters")
    private String pageTitle;

    @Size(max = 500, message = "Additional data must be at most 500 characters")
    private String additionalData;

    public ActivityTrackRequest() {
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

    public WebsiteActivity.ActivityEventType getEventType() {
        return eventType;
    }

    public void setEventType(WebsiteActivity.ActivityEventType eventType) {
        this.eventType = eventType;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public String getAdditionalData() {
        return additionalData;
    }

    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }
}