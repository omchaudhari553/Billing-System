package com.ajalkarbill.website.controller.admin;

import com.ajalkarbill.website.dto.FeatureRequest;
import com.ajalkarbill.website.dto.FeatureResponse;
import com.ajalkarbill.website.service.FeatureServiceAdmin;
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
@RequestMapping("/api/admin/features")
@Tag(name = "Admin Features Management", description = "APIs for managing features including CRUD operations")
public class AdminFeatureController {

    private final FeatureServiceAdmin featureServiceAdmin;

    public AdminFeatureController(FeatureServiceAdmin featureServiceAdmin) {
        this.featureServiceAdmin = featureServiceAdmin;
    }

    @GetMapping
    @Operation(summary = "Get all features", description = "Retrieve all features from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Features retrieved successfully")
    })
    public ResponseEntity<List<FeatureResponse>> getAllFeatures() {
        return ResponseEntity.ok(featureServiceAdmin.getAllFeatures());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feature by ID", description = "Retrieve a specific feature by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feature retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Feature not found")
    })
    public ResponseEntity<FeatureResponse> getFeatureById(
            @Parameter(description = "Feature ID") @PathVariable Long id) {
        return ResponseEntity.ok(featureServiceAdmin.getFeatureById(id));
    }

    @PostMapping
    @Operation(summary = "Create feature", description = "Create a new feature with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Feature created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<FeatureResponse> createFeature(@Valid @RequestBody FeatureRequest request) {
        return new ResponseEntity<>(featureServiceAdmin.createFeature(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update feature", description = "Update an existing feature with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Feature updated successfully"),
            @ApiResponse(responseCode = "404", description = "Feature not found"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<FeatureResponse> updateFeature(@Parameter(description = "Feature ID") @PathVariable Long id,
            @Valid @RequestBody FeatureRequest request) {
        return ResponseEntity.ok(featureServiceAdmin.updateFeature(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete feature", description = "Delete a feature by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Feature deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Feature not found")
    })
    public ResponseEntity<Void> deleteFeature(@Parameter(description = "Feature ID") @PathVariable Long id) {
        featureServiceAdmin.deleteFeature(id);
        return ResponseEntity.noContent().build();
    }
}
