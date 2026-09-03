package com.ajalkarbill.website.controller.publicapi;

import com.ajalkarbill.website.dto.WebsiteSettingsResponse;
import com.ajalkarbill.website.service.WebsiteSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/settings")
@Tag(name = "Public Settings", description = "Public API for retrieving website settings")
public class PublicSettingsController {

    private final WebsiteSettingsService websiteSettingsService;

    public PublicSettingsController(WebsiteSettingsService websiteSettingsService) {
        this.websiteSettingsService = websiteSettingsService;
    }

    @GetMapping
    @Operation(summary = "Get website settings", description = "Retrieve current website settings for public display")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Website settings retrieved successfully")
    })
    public ResponseEntity<WebsiteSettingsResponse> getWebsiteSettings() {
        return ResponseEntity.ok(websiteSettingsService.getWebsiteSettings());
    }
}
