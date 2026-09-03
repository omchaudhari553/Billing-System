package com.ajalkarbill.website.controller.admin;

import com.ajalkarbill.website.dto.AnnouncementRequest;
import com.ajalkarbill.website.dto.AnnouncementResponse;
import com.ajalkarbill.website.service.AnnouncementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/announcements")
@Tag(name = "Admin Announcements Management", description = "APIs for managing announcements including CRUD operations")
public class AdminAnnouncementController {

    private final AnnouncementService announcementService;

    public AdminAnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @GetMapping
    @Operation(summary = "Get all announcements", description = "Retrieve all announcements from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcements retrieved successfully")
    })
    public ResponseEntity<List<AnnouncementResponse>> getAllAnnouncements() {
        return ResponseEntity.ok(announcementService.getAllAnnouncements());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get announcement by ID", description = "Retrieve a specific announcement by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Announcement not found")
    })
    public ResponseEntity<AnnouncementResponse> getAnnouncementById(
            @Parameter(description = "Announcement ID") @PathVariable Long id) {
        return ResponseEntity.ok(announcementService.getAnnouncementById(id));
    }

    @PostMapping
    @Operation(summary = "Create announcement", description = "Create a new announcement with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Announcement created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<AnnouncementResponse> createAnnouncement(@Valid @RequestBody AnnouncementRequest request) {
        return new ResponseEntity<>(announcementService.createAnnouncement(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update announcement", description = "Update an existing announcement with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Announcement updated successfully"),
            @ApiResponse(responseCode = "404", description = "Announcement not found"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<AnnouncementResponse> updateAnnouncement(
            @Parameter(description = "Announcement ID") @PathVariable Long id,
            @Valid @RequestBody AnnouncementRequest request) {
        return ResponseEntity.ok(announcementService.updateAnnouncement(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete announcement", description = "Delete an announcement by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Announcement deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Announcement not found")
    })
    public ResponseEntity<Void> deleteAnnouncement(@Parameter(description = "Announcement ID") @PathVariable Long id) {
        announcementService.deleteAnnouncement(id);
        return ResponseEntity.noContent().build();
    }
}
