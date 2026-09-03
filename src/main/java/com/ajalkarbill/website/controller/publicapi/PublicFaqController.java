package com.ajalkarbill.website.controller.publicapi;

import com.ajalkarbill.website.dto.FAQResponse;
import com.ajalkarbill.website.service.FAQServiceAdmin;
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
@RequestMapping("/api/public/faqs")
@Tag(name = "Public FAQs", description = "Public API for retrieving active FAQs")
public class PublicFaqController {

    private final FAQServiceAdmin faqServiceAdmin;

    public PublicFaqController(FAQServiceAdmin faqServiceAdmin) {
        this.faqServiceAdmin = faqServiceAdmin;
    }

    @GetMapping
    @Operation(summary = "Get active FAQs", description = "Retrieve all active FAQs for public display")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "FAQs retrieved successfully")
    })
    public ResponseEntity<List<FAQResponse>> getActiveFAQs() {
        return ResponseEntity.ok(faqServiceAdmin.getActiveFAQs());
    }
}
