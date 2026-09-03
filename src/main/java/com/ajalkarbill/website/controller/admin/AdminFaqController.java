package com.ajalkarbill.website.controller.admin;

import com.ajalkarbill.website.dto.FAQRequest;
import com.ajalkarbill.website.dto.FAQResponse;
import com.ajalkarbill.website.service.FAQServiceAdmin;
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
@RequestMapping("/api/admin/faqs")
@Tag(name = "Admin FAQ Management", description = "APIs for managing FAQs including CRUD operations")
public class AdminFaqController {

    private final FAQServiceAdmin faqServiceAdmin;

    public AdminFaqController(FAQServiceAdmin faqServiceAdmin) {
        this.faqServiceAdmin = faqServiceAdmin;
    }

    @GetMapping
    @Operation(summary = "Get all FAQs", description = "Retrieve all FAQs from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FAQs retrieved successfully")
    })
    public ResponseEntity<List<FAQResponse>> getAllFAQs() {
        return ResponseEntity.ok(faqServiceAdmin.getAllFAQs());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get FAQ by ID", description = "Retrieve a specific FAQ by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FAQ retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "FAQ not found")
    })
    public ResponseEntity<FAQResponse> getFAQById(@Parameter(description = "FAQ ID") @PathVariable Long id) {
        return ResponseEntity.ok(faqServiceAdmin.getFAQById(id));
    }

    @PostMapping
    @Operation(summary = "Create FAQ", description = "Create a new FAQ with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "FAQ created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<FAQResponse> createFAQ(@Valid @RequestBody FAQRequest request) {
        return new ResponseEntity<>(faqServiceAdmin.createFAQ(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update FAQ", description = "Update an existing FAQ with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FAQ updated successfully"),
            @ApiResponse(responseCode = "404", description = "FAQ not found"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<FAQResponse> updateFAQ(@Parameter(description = "FAQ ID") @PathVariable Long id,
            @Valid @RequestBody FAQRequest request) {
        return ResponseEntity.ok(faqServiceAdmin.updateFAQ(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete FAQ", description = "Delete an FAQ by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "FAQ deleted successfully"),
            @ApiResponse(responseCode = "404", description = "FAQ not found")
    })
    public ResponseEntity<Void> deleteFAQ(@Parameter(description = "FAQ ID") @PathVariable Long id) {
        faqServiceAdmin.deleteFAQ(id);
        return ResponseEntity.noContent().build();
    }
}
