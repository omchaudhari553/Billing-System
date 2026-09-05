package com.ajalkarbill.website.controller.publicapi;

import com.ajalkarbill.website.dto.ActivityTrackRequest;
import com.ajalkarbill.website.dto.VisitorTrackRequest;
import com.ajalkarbill.website.entity.User;
import com.ajalkarbill.website.entity.WebsiteActivity;
import com.ajalkarbill.website.entity.WebsiteVisitor;
import com.ajalkarbill.website.repository.UserRepository;
import com.ajalkarbill.website.service.WebsiteActivityService;
import com.ajalkarbill.website.service.WebsiteVisitorService;
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
@RequestMapping("/api/public/analytics")
@Tag(name = "Public Analytics Tracking", description = "Public APIs for visitor tracking and analytics")
public class PublicAnalyticsController {

    private final WebsiteVisitorService visitorService;
    private final WebsiteActivityService activityService;
    private final UserRepository userRepository;

    public PublicAnalyticsController(WebsiteVisitorService visitorService,
                                     WebsiteActivityService activityService,
                                     UserRepository userRepository) {
        this.visitorService = visitorService;
        this.activityService = activityService;
        this.userRepository = userRepository;
    }

    @PostMapping("/track-visitor")
    @Operation(summary = "Track visitor", description = "Track website visitor information and sessions")
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
        response.put("isNewVisitor", visitor.getVisitCount() == 1);
        response.put("visitCount", visitor.getVisitCount());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/track-activity")
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

    @PostMapping("/associate-visitor")
    @Operation(summary = "Associate visitor with user", description = "Associate a visitor with a registered user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visitor associated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Visitor or user not found")
    })
    public ResponseEntity<Map<String, Object>> associateVisitorWithUser(
            @RequestParam String visitorId,
            Authentication authentication) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Authentication required");
            return ResponseEntity.status(401).body(errorResponse);
        }

        Long userId = getUserIdFromAuthentication(authentication);
        if (userId == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "User not found");
            return ResponseEntity.status(404).body(errorResponse);
        }

        visitorService.associateVisitorWithUser(visitorId, userId);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Visitor associated with user successfully");

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