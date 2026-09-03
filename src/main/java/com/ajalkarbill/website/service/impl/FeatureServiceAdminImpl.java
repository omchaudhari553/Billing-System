package com.ajalkarbill.website.service.impl;

import com.ajalkarbill.website.dto.FeatureRequest;
import com.ajalkarbill.website.dto.FeatureResponse;
import com.ajalkarbill.website.entity.Feature;
import com.ajalkarbill.website.repository.FeatureRepository;
import com.ajalkarbill.website.service.FeatureServiceAdmin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FeatureServiceAdminImpl implements FeatureServiceAdmin {

    private final FeatureRepository featureRepository;

    public FeatureServiceAdminImpl(FeatureRepository featureRepository) {
        this.featureRepository = featureRepository;
    }

    @Override
    public List<FeatureResponse> getAllFeatures() {
        return featureRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public FeatureResponse getFeatureById(Long id) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found with id: " + id));
        return mapToResponse(feature);
    }

    @Override
    public FeatureResponse createFeature(FeatureRequest request) {
        Feature feature = new Feature();
        feature.setTitle(request.getTitle());
        feature.setDescription(request.getDescription());
        feature.setIcon(request.getIcon());
        feature.setIsActive(request.getIsActive());
        feature.setDisplayOrder(request.getDisplayOrder());
        
        Feature savedFeature = featureRepository.save(feature);
        return mapToResponse(savedFeature);
    }

    @Override
    public FeatureResponse updateFeature(Long id, FeatureRequest request) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found with id: " + id));
        
        feature.setTitle(request.getTitle());
        feature.setDescription(request.getDescription());
        feature.setIcon(request.getIcon());
        feature.setIsActive(request.getIsActive());
        feature.setDisplayOrder(request.getDisplayOrder());
        
        Feature savedFeature = featureRepository.save(feature);
        return mapToResponse(savedFeature);
    }

    @Override
    public void deleteFeature(Long id) {
        Feature feature = featureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feature not found with id: " + id));
        featureRepository.delete(feature);
    }

    @Override
    public List<FeatureResponse> getActiveFeatures() {
        return featureRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private FeatureResponse mapToResponse(Feature feature) {
        FeatureResponse response = new FeatureResponse();
        response.setId(feature.getId());
        response.setTitle(feature.getTitle());
        response.setDescription(feature.getDescription());
        response.setIcon(feature.getIcon());
        response.setIsActive(feature.getIsActive());
        response.setDisplayOrder(feature.getDisplayOrder());
        response.setCreatedAt(feature.getCreatedAt());
        response.setUpdatedAt(feature.getUpdatedAt());
        return response;
    }
}
