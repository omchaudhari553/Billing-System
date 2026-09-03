package com.ajalkarbill.website.controller.admin;

import com.ajalkarbill.website.dto.PricingPlanRequest;
import com.ajalkarbill.website.dto.PricingPlanResponse;
import com.ajalkarbill.website.service.PricingPlanService;
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
@RequestMapping("/api/admin/pricing")
@Tag(name = "Admin Pricing Management", description = "APIs for managing pricing plans including CRUD operations and status updates")
public class AdminPricingController {

    private final PricingPlanService pricingPlanService;

    public AdminPricingController(PricingPlanService pricingPlanService) {
        this.pricingPlanService = pricingPlanService;
    }

    @GetMapping
    @Operation(summary = "Get all pricing plans", description = "Retrieve all pricing plans from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pricing plans retrieved successfully")
    })
    public ResponseEntity<List<PricingPlanResponse>> getAllPricingPlans() {
        return ResponseEntity.ok(pricingPlanService.getAllPricingPlans());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pricing plan by ID", description = "Retrieve a specific pricing plan by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pricing plan retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Pricing plan not found")
    })
    public ResponseEntity<PricingPlanResponse> getPricingPlanById(
            @Parameter(description = "Pricing plan ID") @PathVariable Long id) {
        return ResponseEntity.ok(pricingPlanService.getPricingPlanById(id));
    }

    @PostMapping
    @Operation(summary = "Create pricing plan", description = "Create a new pricing plan with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pricing plan created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<PricingPlanResponse> createPricingPlan(@Valid @RequestBody PricingPlanRequest request) {
        return new ResponseEntity<>(pricingPlanService.createPricingPlan(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update pricing plan", description = "Update an existing pricing plan with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pricing plan updated successfully"),
            @ApiResponse(responseCode = "404", description = "Pricing plan not found"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<PricingPlanResponse> updatePricingPlan(
            @Parameter(description = "Pricing plan ID") @PathVariable Long id,
            @Valid @RequestBody PricingPlanRequest request) {
        return ResponseEntity.ok(pricingPlanService.updatePricingPlan(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pricing plan", description = "Delete a pricing plan by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pricing plan deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Pricing plan not found")
    })
    public ResponseEntity<Void> deletePricingPlan(@Parameter(description = "Pricing plan ID") @PathVariable Long id) {
        pricingPlanService.deletePricingPlan(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update pricing plan status", description = "Activate or deactivate a pricing plan")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pricing plan status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Pricing plan not found")
    })
    public ResponseEntity<PricingPlanResponse> updatePricingPlanStatus(
            @Parameter(description = "Pricing plan ID") @PathVariable Long id,
            @Parameter(description = "Active status") @RequestParam Boolean isActive) {
        return ResponseEntity.ok(pricingPlanService.updatePricingPlanStatus(id, isActive));
    }
}
