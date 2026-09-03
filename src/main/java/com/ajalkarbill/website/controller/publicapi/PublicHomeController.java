package com.ajalkarbill.website.controller.publicapi;

import com.ajalkarbill.website.dto.AnnouncementResponse;
import com.ajalkarbill.website.dto.FeatureResponse;
import com.ajalkarbill.website.dto.ModuleResponse;
import com.ajalkarbill.website.dto.WebsiteSettingsResponse;
import com.ajalkarbill.website.service.AnnouncementService;
import com.ajalkarbill.website.service.FeatureServiceAdmin;
import com.ajalkarbill.website.service.ModuleServiceAdmin;
import com.ajalkarbill.website.service.WebsiteSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public/home")
@Tag(name = "Public Home", description = "Public API for home page content including settings, announcements, features, and solutions")
public class PublicHomeController {

    private final WebsiteSettingsService websiteSettingsService;
    private final AnnouncementService announcementService;
    private final FeatureServiceAdmin featureServiceAdmin;
    private final ModuleServiceAdmin moduleServiceAdmin;

    public PublicHomeController(WebsiteSettingsService websiteSettingsService, AnnouncementService announcementService,
            FeatureServiceAdmin featureServiceAdmin, ModuleServiceAdmin moduleServiceAdmin) {
        this.websiteSettingsService = websiteSettingsService;
        this.announcementService = announcementService;
        this.featureServiceAdmin = featureServiceAdmin;
        this.moduleServiceAdmin = moduleServiceAdmin;
    }

    @GetMapping
    @Operation(summary = "Get home page content", description = "Retrieve all content needed for the home page including settings, announcements, features, and solutions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Home content retrieved successfully")
    })
    public ResponseEntity<Map<String, Object>> getHomeContent() {
        WebsiteSettingsResponse settings = websiteSettingsService.getWebsiteSettings();
        List<AnnouncementResponse> announcements = announcementService.getActiveAnnouncements();
        List<FeatureResponse> features = featureServiceAdmin.getActiveFeatures();
        List<ModuleResponse> solutions = moduleServiceAdmin.getActiveModules();

        return ResponseEntity.ok(Map.of(
                "settings", settings,
                "announcements", announcements,
                "features", features,
                "solutions", solutions));
    }
}
