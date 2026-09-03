package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.PricingFeature;
import com.ajalkarbill.website.entity.PricingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricingFeatureRepository extends JpaRepository<PricingFeature, Long> {
    void deleteByPricingPlan(PricingPlan pricingPlan);
}
