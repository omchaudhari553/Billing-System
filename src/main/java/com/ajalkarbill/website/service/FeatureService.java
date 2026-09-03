package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.FeatureDto;
import com.ajalkarbill.website.entity.Feature;
import com.ajalkarbill.website.repository.FeatureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeatureService {
    private final FeatureRepository repository;

    public FeatureService(FeatureRepository repository) {
        this.repository = repository;
    }

    public List<FeatureDto> getAllFeatures() {
        return repository.findAll().stream()
                .map(f -> new FeatureDto(f.getId(), f.getTitle(), f.getDescription(), f.getIcon()))
                .toList();
    }
}
