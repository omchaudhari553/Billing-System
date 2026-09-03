package com.ajalkarbill.website.controller.publicapi;

import com.ajalkarbill.website.dto.ModuleResponse;
import com.ajalkarbill.website.service.ModuleServiceAdmin;
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
@RequestMapping("/api/public/solutions")
@Tag(name = "Public Solutions", description = "Public API for retrieving active solutions/modules")
public class PublicSolutionController {

    private final ModuleServiceAdmin moduleServiceAdmin;

    public PublicSolutionController(ModuleServiceAdmin moduleServiceAdmin) {
        this.moduleServiceAdmin = moduleServiceAdmin;
    }

    @GetMapping
    @Operation(summary = "Get active solutions", description = "Retrieve all active solutions/modules for public display")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Solutions retrieved successfully")
    })
    public ResponseEntity<List<ModuleResponse>> getActiveSolutions() {
        return ResponseEntity.ok(moduleServiceAdmin.getActiveModules());
    }
}
