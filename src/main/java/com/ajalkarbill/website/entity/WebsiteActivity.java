package com.ajalkarbill.website.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "website_activities", indexes = {
    @Index(name = "idx_visitor_id", columnList = "visitorId"),
    @Index(name = "idx_user_id", columnList = "userId"),
    @Index(name = "idx_event_type", columnList = "eventType"),
    @Index(name = "idx_created_at", columnList = "createdAt"),
    @Index(name = "idx_session_id", columnList = "sessionId")
})
public class WebsiteActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String visitorId;

    @Column(length = 64)
    private String sessionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ActivityEventType eventType;

    @Column(length = 500)
    private String pageUrl;

    @Column(length = 200)
    private String pageTitle;

    @Column(length = 500)
    private String additionalData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public enum ActivityEventType {
        PAGE_VIEW,
        REGISTRATION,
        LOGIN,
        CONTACT_ENQUIRY,
        DOWNLOAD,
        FAQ_VIEW,
        FEATURE_VIEW,
        MODULE_VIEW,
        LEAD_SUBMITTED,
        PRICING_VIEW,
        HOME_VIEW
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public WebsiteActivity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public ActivityEventType getEventType() {
        return eventType;
    }

    public void setEventType(ActivityEventType eventType) {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}