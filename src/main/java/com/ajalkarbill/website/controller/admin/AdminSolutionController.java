package com.ajalkarbill.website.controller.admin;

import com.ajalkarbill.website.dto.ModuleRequest;
import com.ajalkarbill.website.dto.ModuleResponse;
import com.ajalkarbill.website.service.ModuleServiceAdmin;
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
@RequestMapping("/api/admin/solutions")
@Tag(name = "Admin Solutions Management", description = "APIs for managing solutions/modules including CRUD operations and status updates")
public class AdminSolutionController {

    private final ModuleServiceAdmin moduleServiceAdmin;

    public AdminSolutionController(ModuleServiceAdmin moduleServiceAdmin) {
        this.moduleServiceAdmin = moduleServiceAdmin;
    }

    @GetMapping
    @Operation(summary = "Get all solutions", description = "Retrieve all solutions/modules from the database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solutions retrieved successfully")
    })
    public ResponseEntity<List<ModuleResponse>> getAllSolutions() {
        return ResponseEntity.ok(moduleServiceAdmin.getAllModules());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get solution by ID", description = "Retrieve a specific solution by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solution retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Solution not found")
    })
    public ResponseEntity<ModuleResponse> getSolutionById(
            @Parameter(description = "Solution ID") @PathVariable Long id) {
        return ResponseEntity.ok(moduleServiceAdmin.getModuleById(id));
    }

    @PostMapping
    @Operation(summary = "Create solution", description = "Create a new solution with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Solution created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<ModuleResponse> createSolution(@Valid @RequestBody ModuleRequest request) {
        return new ResponseEntity<>(moduleServiceAdmin.createModule(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update solution", description = "Update an existing solution with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solution updated successfully"),
            @ApiResponse(responseCode = "404", description = "Solution not found"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<ModuleResponse> updateSolution(@Parameter(description = "Solution ID") @PathVariable Long id,
            @Valid @RequestBody ModuleRequest request) {
        return ResponseEntity.ok(moduleServiceAdmin.updateModule(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete solution", description = "Delete a solution by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Solution deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Solution not found")
    })
    public ResponseEntity<Void> deleteSolution(@Parameter(description = "Solution ID") @PathVariable Long id) {
        moduleServiceAdmin.deleteModule(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update solution status", description = "Activate or deactivate a solution")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solution status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Solution not found")
    })
    public ResponseEntity<ModuleResponse> updateSolutionStatus(
            @Parameter(description = "Solution ID") @PathVariable Long id,
            @Parameter(description = "Active status") @RequestParam Boolean isActive) {
        ModuleResponse response = moduleServiceAdmin.getModuleById(id);
        ModuleRequest request = new ModuleRequest();
        request.setName(response.getName());
        request.setDescription(response.getDescription());
        request.setIcon(response.getIcon());
        request.setIsActive(isActive);
        request.setDisplayOrder(response.getDisplayOrder());
        return ResponseEntity.ok(moduleServiceAdmin.updateModule(id, request));
    }
}
