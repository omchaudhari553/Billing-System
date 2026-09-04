package com.ajalkarbill.website.controller;

import com.ajalkarbill.website.dto.FeatureResponse;
import com.ajalkarbill.website.dto.ModuleResponse;
import com.ajalkarbill.website.service.ModuleServiceAdmin;
import com.ajalkarbill.website.service.impl.ModuleServiceAdminImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {
    private final ModuleServiceAdmin service;
    private final ModuleServiceAdminImpl serviceImpl;

    public ModuleController(ModuleServiceAdmin service, ModuleServiceAdminImpl serviceImpl) {
        this.service = service;
        this.serviceImpl = serviceImpl;
    }

    @GetMapping
    public List<ModuleResponse> getModules() {
        return service.getActiveModules();
    }

    @GetMapping("/{id}")
    public ModuleResponse getModuleById(@PathVariable Long id) {
        return service.getModuleById(id);
    }

    @GetMapping("/{id}/features")
    public List<FeatureResponse> getModuleFeatures(@PathVariable Long id) {
        return serviceImpl.getModuleFeatures(id);
    }

    @GetMapping("/search")
    public List<ModuleResponse> searchModules(@RequestParam String keyword) {
        return serviceImpl.searchModules(keyword);
    }
}
