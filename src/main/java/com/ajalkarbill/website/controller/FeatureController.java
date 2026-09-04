package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.FeatureResponse;
import com.ajalkarbill.website.service.FeatureServiceAdmin;
import com.ajalkarbill.website.service.impl.FeatureServiceAdminImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/features")
public class FeatureController {
    private final FeatureServiceAdmin service;
    private final FeatureServiceAdminImpl serviceImpl;

    public FeatureController(FeatureServiceAdmin service, FeatureServiceAdminImpl serviceImpl) {
        this.service = service;
        this.serviceImpl = serviceImpl;
    }

    @GetMapping
    public List<FeatureResponse> getFeatures() {
        return service.getActiveFeatures();
    }

    @GetMapping("/{id}")
    public FeatureResponse getFeatureById(@PathVariable Long id) {
        return service.getFeatureById(id);
    }

    @GetMapping("/category/{category}")
    public List<FeatureResponse> getFeaturesByCategory(@PathVariable String category) {
        return serviceImpl.getFeaturesByCategory(category);
    }

    @GetMapping("/search")
    public List<FeatureResponse> searchFeatures(@RequestParam String keyword) {
        return serviceImpl.searchFeatures(keyword);
    }
}
