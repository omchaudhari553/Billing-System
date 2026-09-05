package com.ajalkarbill.website.dto;

import com.ajalkarbill.website.entity.WebsiteLead;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LeadRequestDto {

    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    private String fullName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Phone number must be 10 digits starting with 6, 7, 8, or 9")
    private String phoneNumber;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;

    @Size(max = 100, message = "Business name must be at most 100 characters")
    private String businessName;

    @NotNull(message = "Source is required")
    private WebsiteLead.LeadSource source;

    @Size(max = 1000, message = "Message must be at most 1000 characters")
    private String message;

    @Size(max = 64, message = "Visitor ID must be at most 64 characters")
    private String visitorId;

    public LeadRequestDto() {
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

    public WebsiteLead.LeadSource getSource() {
        return source;
    }

    public void setSource(WebsiteLead.LeadSource source) {
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }
}