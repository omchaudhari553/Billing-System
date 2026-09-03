package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.FeatureRequest;
import com.ajalkarbill.website.dto.FeatureResponse;

import java.util.List;

public interface FeatureServiceAdmin {
    List<FeatureResponse> getAllFeatures();
    FeatureResponse getFeatureById(Long id);
    FeatureResponse createFeature(FeatureRequest request);
    FeatureResponse updateFeature(Long id, FeatureRequest request);
    void deleteFeature(Long id);
    List<FeatureResponse> getActiveFeatures();
}
