package com.ajalkarbill.website.service;

import com.ajalkarbill.website.dto.ModuleRequest;
import com.ajalkarbill.website.dto.ModuleResponse;

import java.util.List;

public interface ModuleServiceAdmin {
    List<ModuleResponse> getAllModules();
    ModuleResponse getModuleById(Long id);
    ModuleResponse createModule(ModuleRequest request);
    ModuleResponse updateModule(Long id, ModuleRequest request);
    void deleteModule(Long id);
    List<ModuleResponse> getActiveModules();
}
