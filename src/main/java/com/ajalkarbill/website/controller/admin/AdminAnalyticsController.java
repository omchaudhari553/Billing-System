package com.ajalkarbill.website.controller.admin;

import com.ajalkarbill.website.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/analytics")
@Tag(name = "Admin Analytics", description = "Protected APIs for website analytics and statistics")
public class AdminAnalyticsController {

    private final AnalyticsService analyticsService;

    public AdminAnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/overall")
    @Operation(summary = "Get overall statistics", description = "Retrieve comprehensive website statistics including visitors, activities, and leads")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Overall statistics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<Map<String, Object>> getOverallStatistics() {
        Map<String, Object> stats = analyticsService.getOverallStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/visitors")
    @Operation(summary = "Get visitor statistics", description = "Retrieve visitor-related statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visitor statistics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<Map<String, Object>> getVisitorStatistics() {
        Map<String, Object> stats = analyticsService.getVisitorStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/activities")
    @Operation(summary = "Get activity statistics", description = "Retrieve website activity statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity statistics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<Map<String, Object>> getActivityStatistics() {
        Map<String, Object> stats = analyticsService.getActivityStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/leads")
    @Operation(summary = "Get lead statistics", description = "Retrieve lead generation statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lead statistics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<Map<String, Object>> getLeadStatistics() {
        Map<String, Object> stats = analyticsService.getLeadStatistics();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/engagement")
    @Operation(summary = "Get content engagement", description = "Retrieve content engagement metrics including most viewed pages, features, modules, and FAQs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Content engagement data retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<Map<String, Object>> getContentEngagement() {
        Map<String, Object> engagement = analyticsService.getContentEngagement();
        return ResponseEntity.ok(engagement);
    }

    @GetMapping("/daily/{date}")
    @Operation(summary = "Get daily statistics", description = "Retrieve statistics for a specific date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daily statistics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "400", description = "Invalid date format")
    })
    public ResponseEntity<Map<String, Object>> getDailyStatistics(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        Map<String, Object> stats = analyticsService.getDailyStatistics(date);
        return ResponseEntity.ok(stats);
    }
}