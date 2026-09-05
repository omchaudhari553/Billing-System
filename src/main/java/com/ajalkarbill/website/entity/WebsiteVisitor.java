package com.ajalkarbill.website.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "website_visitors", indexes = {
    @Index(name = "idx_visitor_id", columnList = "visitorId"),
    @Index(name = "idx_session_id", columnList = "sessionId"),
    @Index(name = "idx_created_at", columnList = "createdAt"),
    @Index(name = "idx_user_id", columnList = "userId")
})
public class WebsiteVisitor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String visitorId;

    @Column(length = 64)
    private String sessionId;

    @Column(name = "first_visit_at", nullable = false)
    private LocalDateTime firstVisitAt;

    @Column(name = "last_visit_at", nullable = false)
    private LocalDateTime lastVisitAt;

    @Column(nullable = false)
    private Integer visitCount = 1;

    @Column(length = 500)
    private String lastPageVisited;

    @Column(length = 1000)
    private String userAgent;

    @Column(length = 50)
    private String deviceType;

    @Column(length = 50)
    private String operatingSystem;

    @Column(length = 500)
    private String referrer;

    @Column(name = "last_activity_at")
    private LocalDateTime lastActivityAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (firstVisitAt == null) {
            firstVisitAt = LocalDateTime.now();
        }
        if (lastVisitAt == null) {
            lastVisitAt = LocalDateTime.now();
        }
        if (lastActivityAt == null) {
            lastActivityAt = LocalDateTime.now();
        }
    }

    public WebsiteVisitor() {
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

    public LocalDateTime getFirstVisitAt() {
        return firstVisitAt;
    }

    public void setFirstVisitAt(LocalDateTime firstVisitAt) {
        this.firstVisitAt = firstVisitAt;
    }

    public LocalDateTime getLastVisitAt() {
        return lastVisitAt;
    }

    public void setLastVisitAt(LocalDateTime lastVisitAt) {
        this.lastVisitAt = lastVisitAt;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public String getLastPageVisited() {
        return lastPageVisited;
    }

    public void setLastPageVisited(String lastPageVisited) {
        this.lastPageVisited = lastPageVisited;
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

    public LocalDateTime getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(LocalDateTime lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}