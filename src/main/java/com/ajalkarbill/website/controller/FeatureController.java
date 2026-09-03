package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.FeatureResponse;
import com.ajalkarbill.website.service.FeatureServiceAdmin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/features")
public class FeatureController {
    private final FeatureServiceAdmin service;

    public FeatureController(FeatureServiceAdmin service) {
        this.service = service;
    }

    @GetMapping
    public List<FeatureResponse> getFeatures() {
        return service.getActiveFeatures();
    }
}
