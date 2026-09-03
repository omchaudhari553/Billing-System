package com.ajalkarbill.website.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ContactEnquiryDto(
        @NotBlank(message = "Name is required") String name,
        @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email,
        @NotBlank(message = "Phone is required") String phone,
        @NotBlank(message = "Message is required") String message
) {
}
