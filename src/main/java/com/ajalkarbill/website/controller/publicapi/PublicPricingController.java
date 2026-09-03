package com.ajalkarbill.website.controller.publicapi;

import com.ajalkarbill.website.dto.PricingPlanResponse;
import com.ajalkarbill.website.service.PricingPlanService;
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
@RequestMapping("/api/public/pricing")
@Tag(name = "Public Pricing", description = "Public API for retrieving active pricing plans")
public class PublicPricingController {

    private final PricingPlanService pricingPlanService;

    public PublicPricingController(PricingPlanService pricingPlanService) {
        this.pricingPlanService = pricingPlanService;
    }

    @GetMapping
    @Operation(summary = "Get active pricing plans", description = "Retrieve all active pricing plans for public display")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pricing plans retrieved successfully")
    })
    public ResponseEntity<List<PricingPlanResponse>> getActivePricingPlans() {
        return ResponseEntity.ok(pricingPlanService.getActivePricingPlans());
    }
}
