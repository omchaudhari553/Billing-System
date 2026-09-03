package com.ajalkarbill.website.service.impl;

import com.ajalkarbill.website.dto.PricingPlanRequest;
import com.ajalkarbill.website.dto.PricingPlanResponse;
import com.ajalkarbill.website.entity.PricingFeature;
import com.ajalkarbill.website.entity.PricingPlan;
import com.ajalkarbill.website.repository.PricingFeatureRepository;
import com.ajalkarbill.website.repository.PricingPlanRepository;
import com.ajalkarbill.website.service.PricingPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PricingPlanServiceImpl implements PricingPlanService {

    private final PricingPlanRepository pricingPlanRepository;
    private final PricingFeatureRepository pricingFeatureRepository;

    public PricingPlanServiceImpl(PricingPlanRepository pricingPlanRepository, PricingFeatureRepository pricingFeatureRepository) {
        this.pricingPlanRepository = pricingPlanRepository;
        this.pricingFeatureRepository = pricingFeatureRepository;
    }

    @Override
    public List<PricingPlanResponse> getAllPricingPlans() {
        return pricingPlanRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PricingPlanResponse getPricingPlanById(Long id) {
        PricingPlan plan = pricingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pricing plan not found with id: " + id));
        return mapToResponse(plan);
    }

    @Override
    public PricingPlanResponse createPricingPlan(PricingPlanRequest request) {
        PricingPlan plan = new PricingPlan();
        plan.setName(request.getName());
        plan.setPrice(request.getPrice());
        plan.setBillingPeriod(request.getBillingPeriod());
        plan.setDescription(request.getDescription());
        plan.setIsActive(request.getIsActive());
        plan.setIsPopular(request.getIsPopular());
        plan.setCtaText(request.getCtaText());
        plan.setDisplayOrder(request.getDisplayOrder());
        
        PricingPlan savedPlan = pricingPlanRepository.save(plan);
        
        if (request.getFeatures() != null && !request.getFeatures().isEmpty()) {
            for (String featureText : request.getFeatures()) {
                PricingFeature feature = new PricingFeature(featureText, savedPlan);
                pricingFeatureRepository.save(feature);
            }
        }
        
        return mapToResponse(savedPlan);
    }

    @Override
    public PricingPlanResponse updatePricingPlan(Long id, PricingPlanRequest request) {
        PricingPlan plan = pricingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pricing plan not found with id: " + id));
        
        plan.setName(request.getName());
        plan.setPrice(request.getPrice());
        plan.setBillingPeriod(request.getBillingPeriod());
        plan.setDescription(request.getDescription());
        plan.setIsActive(request.getIsActive());
        plan.setIsPopular(request.getIsPopular());
        plan.setCtaText(request.getCtaText());
        plan.setDisplayOrder(request.getDisplayOrder());
        
        pricingFeatureRepository.deleteByPricingPlan(plan);
        
        if (request.getFeatures() != null && !request.getFeatures().isEmpty()) {
            for (String featureText : request.getFeatures()) {
                PricingFeature feature = new PricingFeature(featureText, plan);
                pricingFeatureRepository.save(feature);
            }
        }
        
        PricingPlan savedPlan = pricingPlanRepository.save(plan);
        return mapToResponse(savedPlan);
    }

    @Override
    public void deletePricingPlan(Long id) {
        PricingPlan plan = pricingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pricing plan not found with id: " + id));
        pricingPlanRepository.delete(plan);
    }

    @Override
    public PricingPlanResponse updatePricingPlanStatus(Long id, Boolean isActive) {
        PricingPlan plan = pricingPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pricing plan not found with id: " + id));
        plan.setIsActive(isActive);
        PricingPlan savedPlan = pricingPlanRepository.save(plan);
        return mapToResponse(savedPlan);
    }

    @Override
    public List<PricingPlanResponse> getActivePricingPlans() {
        return pricingPlanRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PricingPlanResponse mapToResponse(PricingPlan plan) {
        PricingPlanResponse response = new PricingPlanResponse();
        response.setId(plan.getId());
        response.setName(plan.getName());
        response.setPrice(plan.getPrice());
        response.setBillingPeriod(plan.getBillingPeriod());
        response.setDescription(plan.getDescription());
        response.setIsActive(plan.getIsActive());
        response.setIsPopular(plan.getIsPopular());
        response.setCtaText(plan.getCtaText());
        response.setDisplayOrder(plan.getDisplayOrder());
        response.setCreatedAt(plan.getCreatedAt());
        response.setUpdatedAt(plan.getUpdatedAt());
        
        List<String> features = plan.getFeatures().stream()
                .map(PricingFeature::getFeatureText)
                .collect(Collectors.toList());
        response.setFeatures(features);
        
        return response;
    }
}
