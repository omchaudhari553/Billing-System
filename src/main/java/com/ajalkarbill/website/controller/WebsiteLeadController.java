package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.LeadRequestDto;
import com.ajalkarbill.website.service.WebsiteLeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/leads")
@Tag(name = "Website Lead Capture", description = "Public APIs for voluntary lead capture - phone numbers only submitted by visitors")
public class WebsiteLeadController {

    private final WebsiteLeadService leadService;

    public WebsiteLeadController(WebsiteLeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping
    @Operation(summary = "Submit lead", description = "Submit voluntary lead information - visitor provides phone number willingly")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lead submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error or duplicate phone/email")
    })
    public ResponseEntity<Map<String, Object>> submitLead(@Valid @RequestBody LeadRequestDto request) {
        Map<String, Object> response = leadService.createLead(request);
        return ResponseEntity.ok(response);
    }
}