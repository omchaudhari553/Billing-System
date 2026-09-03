package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.ContactEnquiryRequest;
import com.ajalkarbill.website.entity.ContactEnquiry;
import com.ajalkarbill.website.service.ContactEnquiryService;
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
public class ContactEnquiryController {
    
    @Autowired
    private ContactEnquiryService service;

    @PostMapping
    public ResponseEntity<?> submitEnquiry(@Valid @RequestBody ContactEnquiryRequest request, HttpServletRequest httpRequest) {
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
    public ResponseEntity<Page<ContactEnquiry>> getEnquiries(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) ContactEnquiry.EnquiryStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageRequest = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(service.getEnquiries(search, status, startDate, endDate, pageRequest));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestParam ContactEnquiry.EnquiryStatus status) {
        ContactEnquiry updated = service.updateStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnquiry(@PathVariable Long id) {
        service.deleteEnquiry(id);
        return ResponseEntity.ok().build();
    }
}
