package com.ajalkarbill.website.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "website_leads", indexes = {
    @Index(name = "idx_visitor_id", columnList = "visitorId"),
    @Index(name = "idx_phone_number", columnList = "phoneNumber"),
    @Index(name = "idx_email", columnList = "email"),
    @Index(name = "idx_lead_source", columnList = "source"),
    @Index(name = "idx_created_at", columnList = "createdAt")
})
public class WebsiteLead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String visitorId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String businessName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private LeadSource source;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(length = 20)
    private String status = "NEW";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public enum LeadSource {
        FREE_DEMO,
        REQUEST_CALLBACK,
        CONTACT_US,
        DOWNLOAD,
        PRICING_ENQUIRY,
        WHATSAPP,
        NEWSLETTER,
        OTHER
    }

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = "NEW";
        }
    }

    public WebsiteLead() {
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public LeadSource getSource() {
        return source;
    }

    public void setSource(LeadSource source) {
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}