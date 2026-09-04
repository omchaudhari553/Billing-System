package com.ajalkarbill.website.repository;

import com.ajalkarbill.website.entity.Feature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeatureRepository extends JpaRepository<Feature, Long> {
    List<Feature> findByIsActiveTrueOrderByDisplayOrderAsc();

    List<Feature> findByCategory(String category);

    List<Feature> findByCategoryAndIsActiveTrue(String category);

    @Query("SELECT f FROM Feature f WHERE f.isActive = true AND " +
            "(LOWER(f.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Feature> searchFeatures(@Param("keyword") String keyword);
}
