package com.ajalkarbill.website.dto;

import jakarta.validation.constraints.*;

public class ContactEnquiryRequest {
    
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^(?!(?i)null$)[a-zA-Z][a-zA-Z\\s]*$", message = "Invalid name format")
    private String fullName;

    @Pattern(regexp = "^(?!0+$)[a-zA-Z0-9\\s.,&'-]*$", message = "Invalid company name")
    private String companyName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[^0-9].*$", message = "Email cannot start with a number")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "^(?!0{10})\\d{10}$", message = "Must be a valid 10 digit number")
    private String mobileNumber;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String enquiryType;

    @NotBlank(message = "Message is required")
    @Size(min = 20, message = "Message must be at least 20 characters")
    private String message;

    private String pageSource;

    // Getters and Setters
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getEnquiryType() { return enquiryType; }
    public void setEnquiryType(String enquiryType) { this.enquiryType = enquiryType; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPageSource() { return pageSource; }
    public void setPageSource(String pageSource) { this.pageSource = pageSource; }
}
