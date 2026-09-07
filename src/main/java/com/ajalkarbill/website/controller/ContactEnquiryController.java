package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.ContactEnquiryRequest;
import com.ajalkarbill.website.entity.ContactEnquiry;
import com.ajalkarbill.website.service.ContactEnquiryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/enquiries")
@Tag(
        name = "Contact Enquiries",
        description = "APIs for submitting and managing contact enquiries"
)
public class ContactEnquiryController {

    @Autowired
    private ContactEnquiryService service;

    @PostMapping
    @Operation(
            summary = "Submit contact enquiry",
            description = "Public API. Website visitors can submit a contact enquiry without authentication."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enquiry submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<?> submitEnquiry(
            @Valid @RequestBody ContactEnquiryRequest request,
            HttpServletRequest httpRequest) {

        String ipAddress = httpRequest.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = httpRequest.getRemoteAddr();
        }

        ContactEnquiry saved = service.createEnquiry(request, ipAddress);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Enquiry submitted successfully");
        response.put("enquiryId", saved.getEnquiryId());

        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Get contact enquiries",
            description = "Admin-only API. Get enquiries with optional search, status, date filters, pagination and sorting.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enquiries retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required")
    })
    public ResponseEntity<Page<ContactEnquiry>> getEnquiries(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ContactEnquiry.EnquiryStatus status,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return ResponseEntity.ok(
                service.getEnquiries(
                        search,
                        status,
                        startDate,
                        endDate,
                        pageRequest));
    }

    @PutMapping("/{id}/status")
    @Operation(
            summary = "Update enquiry status",
            description = "Admin-only API. Update the status of a contact enquiry.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enquiry status updated successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required"),
            @ApiResponse(responseCode = "404", description = "Enquiry not found")
    })
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam ContactEnquiry.EnquiryStatus status) {

        ContactEnquiry updated = service.updateStatus(id, status);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete contact enquiry",
            description = "Admin-only API. Delete a contact enquiry by ID.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enquiry deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - ADMIN role required"),
            @ApiResponse(responseCode = "404", description = "Enquiry not found")
    })
    public ResponseEntity<?> deleteEnquiry(@PathVariable Long id) {

        service.deleteEnquiry(id);

        return ResponseEntity.ok().build();
    }
}