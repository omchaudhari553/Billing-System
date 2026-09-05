package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.ActivityTrackRequest;
import com.ajalkarbill.website.entity.User;
import com.ajalkarbill.website.entity.WebsiteActivity;
import com.ajalkarbill.website.repository.UserRepository;
import com.ajalkarbill.website.service.WebsiteActivityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/visitors/activity")
@Tag(name = "Website Activity Tracking", description = "Public APIs for tracking website activities and events")
public class WebsiteActivityController {

    private final WebsiteActivityService activityService;
    private final UserRepository userRepository;

    public WebsiteActivityController(WebsiteActivityService activityService, UserRepository userRepository) {
        this.activityService = activityService;
        this.userRepository = userRepository;
    }

    @PostMapping("/track")
    @Operation(summary = "Track activity", description = "Track website activities like page views, downloads, etc.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity tracked successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<Map<String, Object>> trackActivity(@Valid @RequestBody ActivityTrackRequest request,
            Authentication authentication) {
        Long userId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            userId = getUserIdFromAuthentication(authentication);
        }

        WebsiteActivity activity = activityService.trackActivity(request, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Activity tracked successfully");
        response.put("activityId", activity.getId());
        response.put("eventType", activity.getEventType());

        return ResponseEntity.ok(response);
    }

    private Long getUserIdFromAuthentication(Authentication authentication) {
        try {
            if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
                String email = authentication.getName();
                Optional<User> userOpt = userRepository.findByEmail(email);
                return userOpt.map(User::getId).orElse(null);
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }
}