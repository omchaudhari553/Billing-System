package com.ajalkarbill.website.controller.publicapi;

import com.ajalkarbill.website.dto.FeatureResponse;
import com.ajalkarbill.website.service.FeatureServiceAdmin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public/features")
@Tag(name = "Public Features", description = "Public API for retrieving active features")
public class PublicFeatureController {

    private final FeatureServiceAdmin featureServiceAdmin;

    public PublicFeatureController(FeatureServiceAdmin featureServiceAdmin) {
        this.featureServiceAdmin = featureServiceAdmin;
    }

    @GetMapping
    @Operation(summary = "Get active features", description = "Retrieve all active features for public display")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Features retrieved successfully")
    })
    public ResponseEntity<List<FeatureResponse>> getActiveFeatures() {
        return ResponseEntity.ok(featureServiceAdmin.getActiveFeatures());
    }
}
