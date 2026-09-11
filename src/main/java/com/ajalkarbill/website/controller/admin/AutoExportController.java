package com.ajalkarbill.website.controller.admin;

import com.ajalkarbill.website.service.AutoExcelExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/auto-export")
@Tag(name = "Auto Excel Export", description = "APIs for automatic Excel export management")
public class AutoExportController {

    private final AutoExcelExportService autoExcelExportService;

    public AutoExportController(AutoExcelExportService autoExcelExportService) {
        this.autoExcelExportService = autoExcelExportService;
    }

    @PostMapping("/trigger")
    @Operation(summary = "Trigger manual export", description = "Manually trigger Excel export of all data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Export triggered successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "500", description = "Error during export")
    })
    public ResponseEntity<Map<String, Object>> triggerExport() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            autoExcelExportService.exportAllData();
            response.put("success", true);
            response.put("message", "Excel export triggered successfully");
            response.put("exportTime", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Error during export: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/trigger/visitors")
    @Operation(summary = "Trigger visitors export", description = "Manually trigger Excel export of visitors data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Visitors export triggered successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "500", description = "Error during export")
    })
    public ResponseEntity<Map<String, Object>> triggerVisitorsExport() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            autoExcelExportService.exportVisitors();
            response.put("success", true);
            response.put("message", "Visitors Excel export triggered successfully");
            response.put("exportTime", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Error during export: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/trigger/activities")
    @Operation(summary = "Trigger activities export", description = "Manually trigger Excel export of activities data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activities export triggered successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "500", description = "Error during export")
    })
    public ResponseEntity<Map<String, Object>> triggerActivitiesExport() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            autoExcelExportService.exportActivities();
            response.put("success", true);
            response.put("message", "Activities Excel export triggered successfully");
            response.put("exportTime", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Error during export: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/trigger/leads")
    @Operation(summary = "Trigger leads export", description = "Manually trigger Excel export of leads data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leads export triggered successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "500", description = "Error during export")
    })
    public ResponseEntity<Map<String, Object>> triggerLeadsExport() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            autoExcelExportService.exportLeads();
            response.put("success", true);
            response.put("message", "Leads Excel export triggered successfully");
            response.put("exportTime", LocalDateTime.now());
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "Error during export: " + e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/status")
    @Operation(summary = "Get export status", description = "Get current status of auto export configuration and last export time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<Map<String, Object>> getExportStatus() {
        Map<String, Object> response = new HashMap<>();
        
        response.put("enabled", autoExcelExportService.isAutoExportEnabled());
        response.put("exportDirectory", autoExcelExportService.getExportDirectory());
        response.put("lastExportTime", autoExcelExportService.getLastExportTime());
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/toggle")
    @Operation(summary = "Toggle auto export", description = "Enable or disable automatic Excel export")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Auto export toggled successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Authentication required"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<Map<String, Object>> toggleAutoExport(@RequestParam boolean enabled) {
        Map<String, Object> response = new HashMap<>();
        
        autoExcelExportService.setAutoExportEnabled(enabled);
        response.put("success", true);
        response.put("message", "Auto export " + (enabled ? "enabled" : "disabled"));
        response.put("enabled", enabled);
        
        return ResponseEntity.ok(response);
    }
}