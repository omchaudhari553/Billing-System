package com.ajalkarbill.website.service.impl;

import com.ajalkarbill.website.dto.FeatureResponse;
import com.ajalkarbill.website.dto.ModuleRequest;
import com.ajalkarbill.website.dto.ModuleResponse;
import com.ajalkarbill.website.entity.Feature;
import com.ajalkarbill.website.entity.Module;
import com.ajalkarbill.website.repository.ModuleRepository;
import com.ajalkarbill.website.service.ModuleServiceAdmin;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ModuleServiceAdminImpl implements ModuleServiceAdmin {

    private final ModuleRepository moduleRepository;

    public ModuleServiceAdminImpl(ModuleRepository moduleRepository) {
        this.moduleRepository = moduleRepository;
    }

    @Override
    public List<ModuleResponse> getAllModules() {
        return moduleRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ModuleResponse getModuleById(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + id));
        return mapToResponse(module);
    }

    @Override
    public ModuleResponse createModule(ModuleRequest request) {
        Module module = new Module();
        module.setName(request.getName());
        module.setDescription(request.getDescription());
        module.setIcon(request.getIcon());
        module.setIsActive(request.getIsActive());
        module.setDisplayOrder(request.getDisplayOrder());

        Module savedModule = moduleRepository.save(module);
        return mapToResponse(savedModule);
    }

    @Override
    public ModuleResponse updateModule(Long id, ModuleRequest request) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + id));

        module.setName(request.getName());
        module.setDescription(request.getDescription());
        module.setIcon(request.getIcon());
        module.setIsActive(request.getIsActive());
        module.setDisplayOrder(request.getDisplayOrder());

        Module savedModule = moduleRepository.save(module);
        return mapToResponse(savedModule);
    }

    @Override
    public void deleteModule(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + id));
        moduleRepository.delete(module);
    }

    @Override
    public List<ModuleResponse> getActiveModules() {
        return moduleRepository.findByIsActiveTrueOrderByDisplayOrderAsc().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ModuleResponse> searchModules(String keyword) {
        return moduleRepository.searchModules(keyword).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<FeatureResponse> getModuleFeatures(Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found with id: " + moduleId));
        return module.getFeatures().stream()
                .map(this::mapFeatureToResponse)
                .collect(Collectors.toList());
    }

    private ModuleResponse mapToResponse(Module module) {
        ModuleResponse response = new ModuleResponse();
        response.setId(module.getId());
        response.setName(module.getName());
        response.setDescription(module.getDescription());
        response.setIcon(module.getIcon());
        response.setFeatures(module.getFeatures().stream()
                .map(this::mapFeatureToResponse)
                .collect(Collectors.toList()));
        response.setIsActive(module.getIsActive());
        response.setDisplayOrder(module.getDisplayOrder());
        response.setCreatedAt(module.getCreatedAt());
        response.setUpdatedAt(module.getUpdatedAt());
        return response;
    }

    private FeatureResponse mapFeatureToResponse(Feature feature) {
        FeatureResponse response = new FeatureResponse();
        response.setId(feature.getId());
        response.setTitle(feature.getTitle());
        response.setDescription(feature.getDescription());
        response.setIcon(feature.getIcon());
        response.setCategory(feature.getCategory());
        response.setIsActive(feature.getIsActive());
        response.setDisplayOrder(feature.getDisplayOrder());
        response.setCreatedAt(feature.getCreatedAt());
        response.setUpdatedAt(feature.getUpdatedAt());
        return response;
    }
}
