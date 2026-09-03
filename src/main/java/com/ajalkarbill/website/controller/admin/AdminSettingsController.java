package com.ajalkarbill.website.controller.admin;

import com.ajalkarbill.website.dto.WebsiteSettingsRequest;
import com.ajalkarbill.website.dto.WebsiteSettingsResponse;
import com.ajalkarbill.website.service.WebsiteSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/settings")
@Tag(name = "Admin Settings Management", description = "APIs for managing website settings")
public class AdminSettingsController {

    private final WebsiteSettingsService websiteSettingsService;

    public AdminSettingsController(WebsiteSettingsService websiteSettingsService) {
        this.websiteSettingsService = websiteSettingsService;
    }

    @GetMapping
    @Operation(summary = "Get website settings", description = "Retrieve current website settings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Website settings retrieved successfully")
    })
    public ResponseEntity<WebsiteSettingsResponse> getWebsiteSettings() {
        return ResponseEntity.ok(websiteSettingsService.getWebsiteSettings());
    }

    @PutMapping
    @Operation(summary = "Update website settings", description = "Update website settings with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Website settings updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<WebsiteSettingsResponse> updateWebsiteSettings(
            @Valid @RequestBody WebsiteSettingsRequest request) {
        return ResponseEntity.ok(websiteSettingsService.updateWebsiteSettings(request));
    }
}
