package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.PricingPlanRequest;
import com.ajalkarbill.website.dto.PricingPlanResponse;

import java.util.List;

public interface PricingPlanService {
    List<PricingPlanResponse> getAllPricingPlans();
    PricingPlanResponse getPricingPlanById(Long id);
    PricingPlanResponse createPricingPlan(PricingPlanRequest request);
    PricingPlanResponse updatePricingPlan(Long id, PricingPlanRequest request);
    void deletePricingPlan(Long id);
    PricingPlanResponse updatePricingPlanStatus(Long id, Boolean isActive);
    List<PricingPlanResponse> getActivePricingPlans();
}
