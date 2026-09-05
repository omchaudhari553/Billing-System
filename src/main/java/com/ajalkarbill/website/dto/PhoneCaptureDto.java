package com.ajalkarbill.website.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PhoneCaptureDto {

    @NotBlank(message = "Visitor ID is required")
    @Size(max = 64, message = "Visitor ID must be at most 64 characters")
    private String visitorId;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9][0-9]{9}$", message = "Phone number must be 10 digits starting with 6, 7, 8, or 9")
    private String phoneNumber;

    @Size(max = 100, message = "Full name must be at most 100 characters")
    private String fullName;

    @Size(max = 100, message = "Email must be at most 100 characters")
    private String email;

    @Size(max = 100, message = "Business name must be at most 100 characters")
    private String businessName;

    public PhoneCaptureDto() {
    }

    public String getVisitorId() {
        return visitorId;
    }

    public void setVisitorId(String visitorId) {
        this.visitorId = visitorId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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
}
