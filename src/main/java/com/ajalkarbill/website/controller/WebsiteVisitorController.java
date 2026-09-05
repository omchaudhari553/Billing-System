package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.VisitorTrackRequest;
import com.ajalkarbill.website.entity.WebsiteVisitor;
import com.ajalkarbill.website.service.WebsiteVisitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/visitors")
@Tag(name = "Website Visitor Tracking", description = "Public APIs for tracking anonymous website visitors")
public class WebsiteVisitorController {

    private final WebsiteVisitorService visitorService;

    public WebsiteVisitorController(WebsiteVisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @PostMapping("/track")
    @Operation(summary = "Track visitor", description = "Track anonymous visitor information - no personal data collected")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visitor tracked successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<Map<String, Object>> trackVisitor(@Valid @RequestBody VisitorTrackRequest request) {
        WebsiteVisitor visitor = visitorService.trackVisitor(request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Visitor tracked successfully");
        response.put("visitorId", visitor.getVisitorId());
        response.put("visitCount", visitor.getVisitCount());
        
        return ResponseEntity.ok(response);
    }
}